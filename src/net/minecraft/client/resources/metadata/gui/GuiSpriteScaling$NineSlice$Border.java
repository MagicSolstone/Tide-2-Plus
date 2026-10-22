package net.minecraft.client.resources.metadata.gui;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.OptionalInt;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ExtraCodecs;

@Environment(EnvType.CLIENT)
public record GuiSpriteScaling$NineSlice$Border(int left, int top, int right, int bottom) {
   private static final Codec<GuiSpriteScaling$NineSlice$Border> VALUE_CODEC = ExtraCodecs.POSITIVE_INT
      .flatComapMap(size -> new GuiSpriteScaling$NineSlice$Border(size, size, size, size), border -> {
         OptionalInt size = border.unpackValue();
         return size.isPresent() ? DataResult.success(size.getAsInt()) : DataResult.error(() -> "Border has different side sizes");
      });
   private static final Codec<GuiSpriteScaling$NineSlice$Border> RECORD_CODEC = RecordCodecBuilder.create(
      i -> i.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("left").forGetter(GuiSpriteScaling$NineSlice$Border::left),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("top").forGetter(GuiSpriteScaling$NineSlice$Border::top),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("right").forGetter(GuiSpriteScaling$NineSlice$Border::right),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("bottom").forGetter(GuiSpriteScaling$NineSlice$Border::bottom)
         )
         .apply(i, GuiSpriteScaling$NineSlice$Border::new)
   );
   private static final Codec<GuiSpriteScaling$NineSlice$Border> CODEC = Codec.either(VALUE_CODEC, RECORD_CODEC)
      .xmap(Either::unwrap, border -> border.unpackValue().isPresent() ? Either.left(border) : Either.right(border));

   private OptionalInt unpackValue() {
      return this.left() == this.top() && this.top() == this.right() && this.right() == this.bottom() ? OptionalInt.of(this.left()) : OptionalInt.empty();
   }
}
