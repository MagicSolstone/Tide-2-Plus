package net.minecraft.client.renderer.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ExtraCodecs;

@Environment(EnvType.CLIENT)
public record SelectItemModel$SwitchCase<T>(List<T> values, ItemModel$Unbaked model) {
   public static <T> Codec<SelectItemModel$SwitchCase<T>> codec(final Codec<T> valueCodec) {
      return RecordCodecBuilder.create(
         i -> i.group(
               ExtraCodecs.nonEmptyList(ExtraCodecs.compactListCodec(valueCodec)).fieldOf("when").forGetter(SelectItemModel$SwitchCase::values),
               ItemModels.CODEC.fieldOf("model").forGetter(SelectItemModel$SwitchCase::model)
            )
            .apply(i, SelectItemModel$SwitchCase::new)
      );
   }
}
