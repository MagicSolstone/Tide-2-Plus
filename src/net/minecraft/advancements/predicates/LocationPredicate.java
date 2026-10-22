package net.minecraft.advancements.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.levelgen.structure.Structure;

public record LocationPredicate(
   Optional<LocationPredicate$PositionPredicate> position,
   Optional<HolderSet<Biome>> biomes,
   Optional<HolderSet<Structure>> structures,
   Optional<ResourceKey<Level>> dimension,
   Optional<Boolean> smokey,
   Optional<LightPredicate> light,
   Optional<BlockPredicate> block,
   Optional<FluidPredicate> fluid,
   Optional<Boolean> canSeeSky
) {
   public static final Codec<LocationPredicate> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LocationPredicate$PositionPredicate.CODEC.optionalFieldOf("position").forGetter(LocationPredicate::position),
            RegistryCodecs.holderSet(Registries.BIOME).optionalFieldOf("biomes").forGetter(LocationPredicate::biomes),
            RegistryCodecs.holderSet(Registries.STRUCTURE).optionalFieldOf("structures").forGetter(LocationPredicate::structures),
            ResourceKey.codec(Registries.DIMENSION).optionalFieldOf("dimension").forGetter(LocationPredicate::dimension),
            Codec.BOOL.optionalFieldOf("smokey").forGetter(LocationPredicate::smokey),
            LightPredicate.CODEC.optionalFieldOf("light").forGetter(LocationPredicate::light),
            BlockPredicate.CODEC.optionalFieldOf("block").forGetter(LocationPredicate::block),
            FluidPredicate.CODEC.optionalFieldOf("fluid").forGetter(LocationPredicate::fluid),
            Codec.BOOL.optionalFieldOf("can_see_sky").forGetter(LocationPredicate::canSeeSky)
         )
         .apply(i, LocationPredicate::new)
   );

   public boolean matches(final ServerLevel level, final double x, final double y, final double z) {
      if (this.position.isPresent() && !this.position.get().matches(x, y, z)) {
         return false;
      } else if (this.dimension.isPresent() && this.dimension.get() != level.dimension()) {
         return false;
      } else {
         BlockPos pos = BlockPos.containing(x, y, z);
         boolean loaded = level.isLoaded(pos);
         if (!this.biomes.isPresent() || loaded && this.biomes.get().contains(level.getBiome(pos))) {
            if (!this.structures.isPresent() || loaded && level.structureManager().getStructureWithPieceAt(pos, this.structures.get()).isValid()) {
               if (!this.smokey.isPresent() || loaded && this.smokey.get() == CampfireBlock.isSmokeyPos(level, pos)) {
                  if (this.light.isPresent() && !this.light.get().matches(level, pos)) {
                     return false;
                  } else if (this.block.isPresent() && !this.block.get().matches(level, pos)) {
                     return false;
                  } else {
                     return this.fluid.isPresent() && !this.fluid.get().matches(level, pos)
                        ? false
                        : !this.canSeeSky.isPresent() || this.canSeeSky.get() == level.canSeeSky(pos);
                  }
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }
}
