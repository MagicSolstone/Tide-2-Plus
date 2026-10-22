package net.minecraft.data.info;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

record DatapackStructureReport$CustomPackEntry(DatapackStructureReport$Format format, DatapackStructureReport$Entry entry) {
   public static final Codec<DatapackStructureReport$CustomPackEntry> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            DatapackStructureReport$Format.CODEC.fieldOf("format").forGetter(DatapackStructureReport$CustomPackEntry::format),
            DatapackStructureReport$Entry.MAP_CODEC.forGetter(DatapackStructureReport$CustomPackEntry::entry)
         )
         .apply(i, DatapackStructureReport$CustomPackEntry::new)
   );
}
