package net.minecraft.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderSystem$AutoStorageIndexBuffer;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.pipeline.IndexType;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record StagedVertexBuffer$ExecuteInfo(
   GpuBuffer vertexBuffer,
   @Nullable GpuBuffer customIndexBuffer,
   IndexType indexType,
   int baseVertex,
   int firstIndex,
   int indexCount,
   PrimitiveTopology topology
) {
   public GpuBuffer indexBuffer() {
      if (this.customIndexBuffer == null) {
         RenderSystem$AutoStorageIndexBuffer autoIndices = RenderSystem.getSequentialBuffer(this.topology);
         return autoIndices.getBuffer();
      } else {
         return this.customIndexBuffer;
      }
   }
}
