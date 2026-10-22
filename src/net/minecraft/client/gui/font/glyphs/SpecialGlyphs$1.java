package net.minecraft.client.gui.font.glyphs;

import com.mojang.blaze3d.font.GlyphBitmap;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.textures.GpuTexture;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class SpecialGlyphs$1 implements GlyphBitmap {
   SpecialGlyphs$1(final SpecialGlyphs this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public int getPixelWidth() {
      return this.this$0.image.getWidth();
   }

   @Override
   public int getPixelHeight() {
      return this.this$0.image.getHeight();
   }

   @Override
   public float getOversample() {
      return 1.0F;
   }

   @Override
   public void upload(final int x, final int y, final GpuTexture texture) {
      RenderSystem.getDevice().createCommandEncoder().writeToTexture(texture, this.this$0.image, 0, 0, x, y);
   }

   @Override
   public boolean isColored() {
      return true;
   }
}
