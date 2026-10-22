package net.minecraft.client.renderer.texture.atlas.sources;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
import net.minecraft.client.renderer.texture.atlas.SpriteSource$DiscardableLoader;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.client.resources.palette.PaletteMapping;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
record PalettedPermutations$PalettedSpriteSupplier(LazyLoadedImage baseImage, Supplier<PaletteMapping> palette, Identifier permutationLocation)
   implements SpriteSource$DiscardableLoader {
   @Nullable
   @Override
   public SpriteContents get(final SpriteResourceLoader loader) {
      Object var3;
      try {
         NativeImage image = this.baseImage.get().mappedCopy((IntUnaryOperator)this.palette.get());
         return new SpriteContents(this.permutationLocation, new FrameSize(image.getWidth(), image.getHeight()), image);
      } catch (IllegalArgumentException | IOException var7) {
         PalettedPermutations.LOGGER.error("unable to apply palette to {}", this.permutationLocation, var7);
         var3 = null;
      } finally {
         this.baseImage.release();
      }

      return (SpriteContents)var3;
   }

   @Override
   public void discard() {
      this.baseImage.release();
   }
}
