package net.minecraft.client.renderer.texture.atlas.sources;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.NativeImage$Format;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
import net.minecraft.client.renderer.texture.atlas.SpriteSource$DiscardableLoader;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
class Unstitcher$RegionInstance implements SpriteSource$DiscardableLoader {
   private final LazyLoadedImage image;
   private final Unstitcher$Region region;
   private final double xDivisor;
   private final double yDivisor;

   private Unstitcher$RegionInstance(final LazyLoadedImage image, final Unstitcher$Region region, final double xDivisor, final double yDivisor) {
      this.image = image;
      this.region = region;
      this.xDivisor = xDivisor;
      this.yDivisor = yDivisor;
   }

   @Override
   public SpriteContents get(final SpriteResourceLoader loader) {
      try {
         NativeImage fullImage = this.image.get();
         double xScale = fullImage.getWidth() / this.xDivisor;
         double yScale = fullImage.getHeight() / this.yDivisor;
         int x = Mth.floor(this.region.x * xScale);
         int y = Mth.floor(this.region.y * yScale);
         int width = Mth.floor(this.region.width * xScale);
         int height = Mth.floor(this.region.height * yScale);
         NativeImage target = new NativeImage(NativeImage$Format.RGBA, width, height, false);
         fullImage.copyRect(target, x, y, 0, 0, width, height, false, false);
         return new SpriteContents(this.region.sprite, new FrameSize(width, height), target);
      } catch (Exception var16) {
         Unstitcher.LOGGER.error("Failed to unstitch region {}", this.region.sprite, var16);
      } finally {
         this.image.release();
      }

      return MissingTextureAtlasSprite.create();
   }

   @Override
   public void discard() {
      this.image.release();
   }
}
