package net.minecraft.world.level.block.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;

public class BannerPatternLayers$Builder {
   private final Builder<BannerPatternLayers$Layer> layers = ImmutableList.builder();

   @Deprecated
   public BannerPatternLayers$Builder addIfRegistered(
      final HolderGetter<BannerPattern> patternGetter, final ResourceKey<BannerPattern> patternKey, final DyeColor color
   ) {
      Optional<Holder$Reference<BannerPattern>> pattern = patternGetter.get(patternKey);
      if (pattern.isEmpty()) {
         BannerPatternLayers.LOGGER.warn("Unable to find banner pattern with id: '{}'", patternKey.identifier());
         return this;
      } else {
         return this.add(pattern.get(), color);
      }
   }

   public BannerPatternLayers$Builder add(final Holder<BannerPattern> pattern, final DyeColor color) {
      return this.add(new BannerPatternLayers$Layer(pattern, color));
   }

   public BannerPatternLayers$Builder add(final BannerPatternLayers$Layer layer) {
      this.layers.add(layer);
      return this;
   }

   public BannerPatternLayers$Builder addAll(final BannerPatternLayers layers) {
      this.layers.addAll(layers.layers);
      return this;
   }

   public BannerPatternLayers build() {
      return new BannerPatternLayers(this.layers.build());
   }
}
