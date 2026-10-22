package net.minecraft.client.gui.font.providers;

import com.mojang.blaze3d.font.GlyphBitmap;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.textures.GpuTexture;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class BitmapProvider$Glyph$1 implements GlyphBitmap {
   BitmapProvider$Glyph$1(final BitmapProvider$Glyph this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public float getOversample() {
      return 1.0F / this.this$0.scale;
   }

   @Override
   public int getPixelWidth() {
      return this.this$0.width;
   }

   @Override
   public int getPixelHeight() {
      return this.this$0.height;
   }

   @Override
   public float getBearingTop() {
      return this.this$0.ascent;
   }

   @Override
   public void upload(final int x, final int y, final GpuTexture texture) {
      RenderSystem.getDevice()
         .createCommandEncoder()
         .copyBufferToTexture(
            this.this$0.imageData.gpuData().slice(),
            this.this$0.offsetX,
            this.this$0.offsetY,
            this.this$0.imageData.image.getWidth(),
            this.this$0.imageData.image.getHeight(),
            texture,
            x,
            y,
            this.this$0.width,
            this.this$0.height,
            0,
            0
         );
   }

   @Override
   public boolean isColored() {
      return this.this$0.imageData.image.format().components() > 1;
   }
}
