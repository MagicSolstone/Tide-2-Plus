package com.mojang.blaze3d.pipeline;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class MainTarget$Dimension {
   public final int width;
   public final int height;

   private MainTarget$Dimension(final int width, final int height) {
      this.width = width;
      this.height = height;
   }

   private static List<MainTarget$Dimension> listWithFallback(final int width, final int height) {
      RenderSystem.assertOnRenderThread();
      int maxTextureSize = RenderSystem.getDevice().getDeviceInfo().limits().maxTextureSize();
      return width > 0 && width <= maxTextureSize && height > 0 && height <= maxTextureSize
         ? ImmutableList.of(new MainTarget$Dimension(width, height), MainTarget.DEFAULT_DIMENSIONS)
         : ImmutableList.of(MainTarget.DEFAULT_DIMENSIONS);
   }

   @Override
   public boolean equals(final Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         MainTarget$Dimension that = (MainTarget$Dimension)other;
         return this.width == that.width && this.height == that.height;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.width, this.height);
   }

   @Override
   public String toString() {
      return this.width + "x" + this.height;
   }
}
