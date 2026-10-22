package com.mojang.renderpearl.backend.opengl;

import com.mojang.blaze3d.systems.ScissorState;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.GpuQueryPool;
import com.mojang.renderpearl.api.pipeline.IndexType;
import com.mojang.renderpearl.backend.api.BackendRenderPipeline;
import com.mojang.renderpearl.backend.api.RenderPassBackend;
import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceList;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;
import org.lwjgl.PointerBuffer;

@Environment(EnvType.CLIENT)
class GlRenderPass implements RenderPassBackend {
   private final GlCommandEncoder encoder;
   private final GlDevice device;
   private final ScissorState defaultScissorState;
   @Nullable
   protected GlRenderPipeline pipeline;
   protected final GpuBufferSlice[] vertexBuffers = new GpuBufferSlice[16];
   protected boolean vertexBufferDirty = true;
   @Nullable
   protected GpuBuffer indexBuffer;
   protected IndexType indexType = IndexType.INT;
   protected boolean indexBufferDirty = false;
   private final ScissorState scissorState = new ScissorState();
   protected boolean scissorStateDirty = true;
   protected final ReferenceList<Object> uniforms = new ReferenceArrayList();
   protected final BooleanList dirtyUniforms = new BooleanArrayList();
   protected boolean anyUniformDirty = false;
   @Nullable
   protected GpuBufferSlice pushConstants;
   protected boolean pushConstantsDirty = false;
   protected final int colorAttachmentCount;

   public GlRenderPass(final GlCommandEncoder encoder, final GlDevice device, final int colorAttachmentCount, final ScissorState defaultScissorState) {
      this.encoder = encoder;
      this.device = device;
      this.colorAttachmentCount = colorAttachmentCount;
      this.defaultScissorState = defaultScissorState;
      this.scissorState.setFrom(defaultScissorState);
   }

   @Override
   public void pushDebugGroup(final Supplier<String> label) {
      this.device.debugLabels().pushDebugGroup(label);
   }

   @Override
   public void popDebugGroup() {
      this.device.debugLabels().popDebugGroup();
   }

   @Override
   public void setPipeline(final BackendRenderPipeline pipeline) {
      if (!(pipeline instanceof GlRenderPipeline glRenderPipeline)) {
         throw new IllegalArgumentException("Pipeline must be instance of GlRenderPipeline");
      } else {
         if (this.pipeline == null || this.pipeline != pipeline) {
            this.uniforms.clear();
            this.uniforms.size(glRenderPipeline.program().uniformCount());
            this.dirtyUniforms.clear();
            this.dirtyUniforms.size(glRenderPipeline.program().uniformCount());

            for (int i = 0; i < this.dirtyUniforms.size(); i++) {
               this.dirtyUniforms.set(i, true);
            }

            this.anyUniformDirty = true;
         }

         this.pipeline = glRenderPipeline;
         this.vertexBufferDirty = true;
         this.indexBufferDirty = this.indexBuffer != null;
      }
   }

   @Override
   public void setUniform(final int index, @Nullable final Object value) {
      this.uniforms.set(index, value);
      this.dirtyUniforms.set(index, true);
      this.anyUniformDirty = true;
   }

   @Override
   public void pushConstants(final ByteBuffer value) {
      this.pushConstants = this.encoder.transientMemory().uploadGpu(value, (long)this.device.getDeviceInfo().limits().minUniformOffsetAlignment(), 128);
      this.pushConstantsDirty = true;
   }

   @Override
   public void enableScissor(final int x, final int y, final int width, final int height) {
      this.scissorState.enable(x, y, width, height);
      this.scissorStateDirty = true;
   }

   @Override
   public void disableScissor() {
      this.scissorState.setFrom(this.defaultScissorState);
      this.scissorStateDirty = true;
   }

   public boolean isScissorEnabled() {
      return this.scissorState.enabled();
   }

   public int getScissorX() {
      return this.scissorState.x();
   }

   public int getScissorY() {
      return this.scissorState.y();
   }

   public int getScissorWidth() {
      return this.scissorState.width();
   }

   public int getScissorHeight() {
      return this.scissorState.height();
   }

   @Override
   public void setVertexBuffer(final int slot, @Nullable final GpuBufferSlice vertexBuffer) {
      GpuBuffer inputBuffer = vertexBuffer != null ? vertexBuffer.buffer() : null;
      GpuBuffer existingBuffer = this.vertexBuffers[slot] != null ? this.vertexBuffers[slot].buffer() : null;
      long inputOffset = vertexBuffer != null ? vertexBuffer.offset() : 0L;
      long exitingOffset = this.vertexBuffers[slot] != null ? this.vertexBuffers[slot].offset() : 0L;
      this.vertexBufferDirty |= inputBuffer != existingBuffer || inputOffset != exitingOffset;
      this.vertexBuffers[slot] = vertexBuffer;
   }

   @Override
   public void setIndexBuffer(@Nullable final GpuBuffer indexBuffer, final IndexType indexType) {
      this.indexBuffer = indexBuffer;
      this.indexType = indexType;
      this.indexBufferDirty = true;
   }

   @Override
   public void drawIndexed(final int indexCount, final int instanceCount, final int firstIndex, final int vertexOffset, final int firstInstance) {
      this.encoder.executeDraw(this, vertexOffset, firstIndex, indexCount, this.indexType, instanceCount, firstInstance);
   }

   @Override
   public void multiDrawIndexed(final IntBuffer drawParameters, final int instanceCount, final int firstInstance, final int drawCount) {
      throw new UnsupportedOperationException("OpenGL does not support the multiDrawDirectInterleaved device feature");
   }

   @Override
   public void multiDrawIndexed(final PointerBuffer firstIndexOffsets, final IntBuffer indexCounts, final IntBuffer vertexOffsets, final int drawCount) {
      this.encoder.executeDraws(this, this.indexType, firstIndexOffsets, indexCounts, vertexOffsets, drawCount);
   }

   @Override
   public void drawIndexedIndirect(final GpuBufferSlice commands, final int drawCount) {
      this.encoder.executeDrawIndirect(this, this.indexType, (GlBuffer)commands.buffer(), commands.offset(), drawCount);
   }

   @Override
   public void draw(final int vertexCount, final int instanceCount, final int firstVertex, final int firstInstance) {
      this.encoder.executeDraw(this, firstVertex, 0, vertexCount, null, instanceCount, firstInstance);
   }

   @Override
   public void multiDraw(final IntBuffer drawParameters, final int instanceCount, final int firstInstance, final int drawCount) {
      throw new UnsupportedOperationException("OpenGL does not support the multiDrawDirectInterleaved device feature");
   }

   @Override
   public void multiDraw(final IntBuffer firstVertices, final IntBuffer vertexCounts, final int drawCount) {
      this.encoder.executeDraws(this, null, null, vertexCounts, firstVertices, drawCount);
   }

   @Override
   public void drawIndirect(final GpuBufferSlice commands, final int drawCount) {
      this.encoder.executeDrawIndirect(this, null, (GlBuffer)commands.buffer(), commands.offset(), drawCount);
   }

   @Override
   public void writeTimestamp(final GpuQueryPool pool, final int index) {
      ((GlQueryPool)pool).writeTimestamp(index);
   }
}
