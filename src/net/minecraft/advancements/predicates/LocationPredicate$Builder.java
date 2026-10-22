package net.minecraft.advancements.predicates;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

public class LocationPredicate$Builder {
   private MinMaxBounds$Doubles x = MinMaxBounds$Doubles.ANY;
   private MinMaxBounds$Doubles y = MinMaxBounds$Doubles.ANY;
   private MinMaxBounds$Doubles z = MinMaxBounds$Doubles.ANY;
   private Optional<HolderSet<Biome>> biomes = Optional.empty();
   private Optional<HolderSet<Structure>> structures = Optional.empty();
   private Optional<ResourceKey<Level>> dimension = Optional.empty();
   private Optional<Boolean> smokey = Optional.empty();
   private Optional<LightPredicate> light = Optional.empty();
   private Optional<BlockPredicate> block = Optional.empty();
   private Optional<FluidPredicate> fluid = Optional.empty();
   private Optional<Boolean> canSeeSky = Optional.empty();

   public static LocationPredicate$Builder location() {
      return new LocationPredicate$Builder();
   }

   public static LocationPredicate$Builder inBiome(final Holder<Biome> biome) {
      return location().setBiomes(HolderSet.direct(biome));
   }

   public static LocationPredicate$Builder inDimension(final ResourceKey<Level> dimension) {
      return location().setDimension(dimension);
   }

   public static LocationPredicate$Builder inStructure(final Holder<Structure> structure) {
      return location().setStructures(HolderSet.direct(structure));
   }

   public static LocationPredicate$Builder atYLocation(final MinMaxBounds$Doubles yLocation) {
      return location().setY(yLocation);
   }

   public LocationPredicate$Builder setX(final MinMaxBounds$Doubles x) {
      this.x = x;
      return this;
   }

   public LocationPredicate$Builder setY(final MinMaxBounds$Doubles y) {
      this.y = y;
      return this;
   }

   public LocationPredicate$Builder setZ(final MinMaxBounds$Doubles z) {
      this.z = z;
      return this;
   }

   public LocationPredicate$Builder setBiomes(final HolderSet<Biome> biomes) {
      this.biomes = Optional.of(biomes);
      return this;
   }

   public LocationPredicate$Builder setStructures(final HolderSet<Structure> structures) {
      this.structures = Optional.of(structures);
      return this;
   }

   public LocationPredicate$Builder setDimension(final ResourceKey<Level> dimension) {
      this.dimension = Optional.of(dimension);
      return this;
   }

   public LocationPredicate$Builder setLight(final LightPredicate$Builder light) {
      this.light = Optional.of(light.build());
      return this;
   }

   public LocationPredicate$Builder setBlock(final BlockPredicate$Builder block) {
      this.block = Optional.of(block.build());
      return this;
   }

   public LocationPredicate$Builder setFluid(final FluidPredicate$Builder fluid) {
      this.fluid = Optional.of(fluid.build());
      return this;
   }

   public LocationPredicate$Builder setSmokey(final boolean smokey) {
      this.smokey = Optional.of(smokey);
      return this;
   }

   public LocationPredicate$Builder setCanSeeSky(final boolean canSeeSky) {
      this.canSeeSky = Optional.of(canSeeSky);
      return this;
   }

   public LocationPredicate build() {
      Optional<LocationPredicate$PositionPredicate> position = LocationPredicate$PositionPredicate.of(this.x, this.y, this.z);
      return new LocationPredicate(position, this.biomes, this.structures, this.dimension, this.smokey, this.light, this.block, this.fluid, this.canSeeSky);
   }
}
