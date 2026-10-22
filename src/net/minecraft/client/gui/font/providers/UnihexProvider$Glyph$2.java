package net.minecraft.client.gui.font.providers;

import com.mojang.blaze3d.font.GlyphBitmap;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.textures.GpuTexture;
import java.nio.IntBuffer;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.system.MemoryUtil;

@Environment(EnvType.CLIENT)
class UnihexProvider$Glyph$2 implements GlyphBitmap {
   UnihexProvider$Glyph$2(final UnihexProvider$Glyph this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public float getOversample() {
      return 2.0F;
   }

   @Override
   public int getPixelWidth() {
      return this.this$0.width();
   }

   @Override
   public int getPixelHeight() {
      return 16;
   }

   @Override
   public void upload(final int x, final int y, final GpuTexture texture) {
      IntBuffer targetBuffer = MemoryUtil.memAllocInt(this.this$0.width() * 16);
      UnihexProvider.unpackBitsToBytes(targetBuffer, this.this$0.contents, this.this$0.left, this.this$0.right);
      targetBuffer.rewind();
      RenderSystem.getDevice().createCommandEncoder().writeToTexture(texture, MemoryUtil.memByteBuffer(targetBuffer), 0, 0, x, y, this.this$0.width(), 16);
      MemoryUtil.memFree(targetBuffer);
   }

   @Override
   public boolean isColored() {
      return true;
   }
}
