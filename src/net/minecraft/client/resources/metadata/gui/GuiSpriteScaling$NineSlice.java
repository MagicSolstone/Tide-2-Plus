package net.minecraft.client.resources.metadata.gui;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ExtraCodecs;

@Environment(EnvType.CLIENT)
public record GuiSpriteScaling$NineSlice(int width, int height, GuiSpriteScaling$NineSlice$Border border, boolean stretchInner) implements GuiSpriteScaling {
   public static final MapCodec<GuiSpriteScaling$NineSlice> CODEC = RecordCodecBuilder.mapCodec(
         i -> i.group(
               ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(GuiSpriteScaling$NineSlice::width),
               ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(GuiSpriteScaling$NineSlice::height),
               GuiSpriteScaling$NineSlice$Border.CODEC.fieldOf("border").forGetter(GuiSpriteScaling$NineSlice::border),
               Codec.BOOL.optionalFieldOf("stretch_inner", false).forGetter(GuiSpriteScaling$NineSlice::stretchInner)
            )
            .apply(i, GuiSpriteScaling$NineSlice::new)
      )
      .validate(GuiSpriteScaling$NineSlice::validate);

   private static DataResult<GuiSpriteScaling$NineSlice> validate(final GuiSpriteScaling$NineSlice nineSlice) {
      GuiSpriteScaling$NineSlice$Border border = nineSlice.border();
      if (border.left() + border.right() >= nineSlice.width()) {
         return DataResult.error(
            () -> "Nine-sliced texture has no horizontal center slice: " + border.left() + " + " + border.right() + " >= " + nineSlice.width()
         );
      } else {
         return border.top() + border.bottom() >= nineSlice.height()
            ? DataResult.error(
               () -> "Nine-sliced texture has no vertical center slice: " + border.top() + " + " + border.bottom() + " >= " + nineSlice.height()
            )
            : DataResult.success(nineSlice);
      }
   }

   @Override
   public GuiSpriteScaling$Type type() {
      return GuiSpriteScaling$Type.NINE_SLICE;
   }
}
