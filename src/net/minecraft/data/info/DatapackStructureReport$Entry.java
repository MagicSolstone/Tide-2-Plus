package net.minecraft.data.info;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

record DatapackStructureReport$Entry(boolean elements, boolean tags, boolean stable) {
   public static final MapCodec<DatapackStructureReport$Entry> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Codec.BOOL.fieldOf("elements").forGetter(DatapackStructureReport$Entry::elements),
            Codec.BOOL.fieldOf("tags").forGetter(DatapackStructureReport$Entry::tags),
            Codec.BOOL.fieldOf("stable").forGetter(DatapackStructureReport$Entry::stable)
         )
         .apply(i, DatapackStructureReport$Entry::new)
   );
   public static final Codec<DatapackStructureReport$Entry> CODEC = MAP_CODEC.codec();
}
