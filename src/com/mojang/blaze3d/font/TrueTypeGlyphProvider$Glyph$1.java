package com.mojang.blaze3d.font;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.NativeImage$Format;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.textures.GpuTexture;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.util.freetype.FT_Face;

@Environment(EnvType.CLIENT)
class TrueTypeGlyphProvider$Glyph$1 implements GlyphBitmap {
   TrueTypeGlyphProvider$Glyph$1(final TrueTypeGlyphProvider$Glyph this$1) {
      Objects.requireNonNull(this$1);
      this.this$1 = this$1;
      super();
   }

   @Override
   public int getPixelWidth() {
      return this.this$1.width;
   }

   @Override
   public int getPixelHeight() {
      return this.this$1.height;
   }

   @Override
   public float getOversample() {
      return this.this$1.this$0.oversample;
   }

   @Override
   public float getBearingLeft() {
      return this.this$1.bearingX;
   }

   @Override
   public float getBearingTop() {
      return this.this$1.bearingY;
   }

   @Override
   public void upload(final int x, final int y, final GpuTexture texture) {
      FT_Face face = this.this$1.this$0.validateFontOpen();

      try (NativeImage image = new NativeImage(NativeImage$Format.LUMINANCE, this.this$1.width, this.this$1.height, false)) {
         if (image.copyFromFont(face, this.this$1.index)) {
            RenderSystem.getDevice().createCommandEncoder().writeToTexture(texture, image, 0, 0, x, y);
         }
      }
   }

   @Override
   public boolean isColored() {
      return false;
   }
}
