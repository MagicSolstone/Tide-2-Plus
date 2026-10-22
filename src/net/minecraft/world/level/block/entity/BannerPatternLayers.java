package net.minecraft.world.level.block.entity;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item$TooltipContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.slf4j.Logger;

public record BannerPatternLayers(List<BannerPatternLayers$Layer> layers) implements TooltipProvider {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final BannerPatternLayers EMPTY = new BannerPatternLayers(List.of());
   public static final Codec<BannerPatternLayers> CODEC = BannerPatternLayers$Layer.CODEC.listOf().xmap(BannerPatternLayers::new, BannerPatternLayers::layers);
   public static final StreamCodec<RegistryFriendlyByteBuf, BannerPatternLayers> STREAM_CODEC = BannerPatternLayers$Layer.STREAM_CODEC
      .apply(ByteBufCodecs.list())
      .map(BannerPatternLayers::new, BannerPatternLayers::layers);

   public BannerPatternLayers removeLast() {
      return new BannerPatternLayers(List.copyOf(this.layers.subList(0, this.layers.size() - 1)));
   }

   @Override
   public void addToTooltip(final Item$TooltipContext context, final Consumer<Component> consumer, final TooltipFlag flag, final DataComponentGetter components) {
      for (int i = 0; i < Math.min(this.layers().size(), 6); i++) {
         consumer.accept(this.layers().get(i).description().withStyle(ChatFormatting.GRAY));
      }
   }
}
