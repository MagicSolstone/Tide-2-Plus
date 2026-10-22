package net.minecraft.world.level.levelgen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.ExtraCodecs;

public record StructureSet$StructureSelectionEntry(Holder<Structure> structure, int weight) {
   public static final Codec<StructureSet$StructureSelectionEntry> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Structure.CODEC.fieldOf("structure").forGetter(StructureSet$StructureSelectionEntry::structure),
            ExtraCodecs.POSITIVE_INT.fieldOf("weight").forGetter(StructureSet$StructureSelectionEntry::weight)
         )
         .apply(i, StructureSet$StructureSelectionEntry::new)
   );
}
