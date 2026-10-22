package net.minecraft.world.level.block.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;

public record BannerPatternLayers$Layer(Holder<BannerPattern> pattern, DyeColor color) {
   public static final Codec<BannerPatternLayers$Layer> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            BannerPattern.CODEC.fieldOf("pattern").forGetter(BannerPatternLayers$Layer::pattern),
            DyeColor.CODEC.fieldOf("color").forGetter(BannerPatternLayers$Layer::color)
         )
         .apply(i, BannerPatternLayers$Layer::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, BannerPatternLayers$Layer> STREAM_CODEC = StreamCodec.composite(
      BannerPattern.STREAM_CODEC, BannerPatternLayers$Layer::pattern, DyeColor.STREAM_CODEC, BannerPatternLayers$Layer::color, BannerPatternLayers$Layer::new
   );

   public MutableComponent description() {
      String prefix = this.pattern.value().translationKey();
      return Component.translatable(prefix + "." + this.color.getName());
   }
}
