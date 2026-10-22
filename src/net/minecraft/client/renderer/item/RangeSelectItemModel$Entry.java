package net.minecraft.client.renderer.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Comparator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record RangeSelectItemModel$Entry(float threshold, ItemModel$Unbaked model) {
   public static final Codec<RangeSelectItemModel$Entry> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.FLOAT.fieldOf("threshold").forGetter(RangeSelectItemModel$Entry::threshold),
            ItemModels.CODEC.fieldOf("model").forGetter(RangeSelectItemModel$Entry::model)
         )
         .apply(i, RangeSelectItemModel$Entry::new)
   );
   public static final Comparator<RangeSelectItemModel$Entry> BY_THRESHOLD = Comparator.comparingDouble(RangeSelectItemModel$Entry::threshold);
}
