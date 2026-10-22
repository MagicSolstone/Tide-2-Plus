package net.minecraft.world.level.levelgen.structure.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.StructureSet;

@Deprecated
public record AbstractSpreadingStructurePlacement$ExclusionZone(Holder<StructureSet> otherSet, int chunkCount) {
   public static final Codec<AbstractSpreadingStructurePlacement$ExclusionZone> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            RegistryCodecs.holder(Registries.STRUCTURE_SET, StructureSet.DIRECT_CODEC, false)
               .fieldOf("other_set")
               .forGetter(AbstractSpreadingStructurePlacement$ExclusionZone::otherSet),
            Codec.intRange(1, 16).fieldOf("chunk_count").forGetter(AbstractSpreadingStructurePlacement$ExclusionZone::chunkCount)
         )
         .apply(i, AbstractSpreadingStructurePlacement$ExclusionZone::new)
   );

   private boolean isPlacementForbidden(final ChunkGeneratorStructureState state, final int sourceX, final int sourceZ) {
      return state.hasStructureChunkInRange(this.otherSet, sourceX, sourceZ, this.chunkCount);
   }
}
