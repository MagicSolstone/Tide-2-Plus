package net.minecraft.client.gui.font.providers;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice$MappedView;
import java.nio.ByteBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import org.lwjgl.system.MemoryUtil;

@Environment(EnvType.CLIENT)
class BitmapProvider$ImageDataHolder implements AutoCloseable {
   private final Identifier identifier;
   private final NativeImage image;
   @Nullable
   private GpuBuffer gpuBuffer = null;

   private BitmapProvider$ImageDataHolder(final Identifier identifier, final NativeImage image) {
      this.identifier = identifier;
      this.image = image;
   }

   private GpuBuffer gpuData() {
      if (this.gpuBuffer == null) {
         ByteBuffer imageBytes = this.image.getPixelBytes();
         this.gpuBuffer = RenderSystem.getDevice().createBuffer(() -> this.identifier + " staging buffer", 22, imageBytes.remaining());

         try (GpuBufferSlice$MappedView mappedView = this.gpuBuffer.map(false, true)) {
            MemoryUtil.memCopy(imageBytes, mappedView.data());
         }
      }

      return this.gpuBuffer;
   }

   @Override
   public void close() {
      this.image.close();
      if (this.gpuBuffer != null) {
         this.gpuBuffer.close();
      }
   }
}
