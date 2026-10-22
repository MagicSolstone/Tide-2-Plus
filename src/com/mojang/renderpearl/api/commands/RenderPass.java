package com.mojang.renderpearl.api.commands;

import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.pipeline.CompiledRenderPipeline;
import com.mojang.renderpearl.api.pipeline.IndexType;
import com.mojang.renderpearl.api.textures.GpuSampler;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import com.mojang.renderpearl.util.UncheckedAutoCloseable;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Collection;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;
import org.lwjgl.PointerBuffer;

@Environment(EnvType.CLIENT)
public interface RenderPass extends UncheckedAutoCloseable {
   int MAX_VERTEX_BUFFERS = 16;
   int INDIRECT_DRAW_SIZE = 16;
   int INDIRECT_INDEXED_DRAW_SIZE = 20;

   void pushDebugGroup(Supplier<String> label);

   void popDebugGroup();

   void writeTimestamp(GpuQueryPool pool, int index);

   void setPipeline(CompiledRenderPipeline pipeline);

   void setUniform(String name, @Nullable GpuTextureView textureView, @Nullable GpuSampler sampler);

   void setUniform(String name, GpuBuffer value);

   void setUniform(String name, GpuBufferSlice value);

   void pushConstants(ByteBuffer value);

   void enableScissor(int x, int y, int width, int height);

   void disableScissor();

   void setVertexBuffer(int slot, @Nullable GpuBufferSlice vertexBuffer);

   void setIndexBuffer(GpuBuffer indexBuffer, IndexType indexType);

   void drawIndexed(int indexCount, int instanceCount, int firstIndex, int vertexOffset, int firstInstance);

   void multiDrawIndexed(IntBuffer drawParameters, int instanceCount, int firstInstance, int drawCount);

   void multiDrawIndexed(PointerBuffer firstIndexOffsets, IntBuffer indexCounts, IntBuffer vertexOffsets, int drawCount);

   void drawIndexedIndirect(GpuBufferSlice commands, int drawCount);

   <T> void drawMultipleIndexed(
      Collection<RenderPass$Draw<T>> draws,
      @Nullable GpuBuffer defaultIndexBuffer,
      @Nullable IndexType defaultIndexType,
      Collection<String> dynamicUniforms,
      T uniformArgument
   );

   void draw(int vertexCount, int instanceCount, int firstVertex, int firstInstance);

   void multiDraw(IntBuffer drawParameters, int instanceCount, int firstInstance, int drawCount);

   void multiDraw(IntBuffer firstVertices, IntBuffer vertexCounts, int drawCount);

   void drawIndirect(GpuBufferSlice commands, final int drawCount);
}
