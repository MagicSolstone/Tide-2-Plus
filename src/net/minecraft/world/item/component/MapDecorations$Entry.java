package net.minecraft.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;

public record MapDecorations$Entry(Holder<MapDecorationType> type, double x, double z, float rotation) {
   public static final Codec<MapDecorations$Entry> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            MapDecorationType.CODEC.fieldOf("type").forGetter(MapDecorations$Entry::type),
            Codec.DOUBLE.fieldOf("x").forGetter(MapDecorations$Entry::x),
            Codec.DOUBLE.fieldOf("z").forGetter(MapDecorations$Entry::z),
            Codec.FLOAT.fieldOf("rotation").forGetter(MapDecorations$Entry::rotation)
         )
         .apply(i, MapDecorations$Entry::new)
   );
}
