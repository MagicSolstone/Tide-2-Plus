package net.minecraft.client.renderer.chunk;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.UberGpuBuffer$UploadCallback;
import com.mojang.blaze3d.vertex.VertexSorting;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.RotatingSectionStorage$Value;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Util;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class SectionRenderDispatcher$RenderSection implements RotatingSectionStorage$Value {
   public final int index;
   public final AtomicReference<SectionMesh> sectionMesh;
   private SectionRenderDispatcher$RenderSection$CompileTask lastCompileTask;
   private SectionRenderDispatcher$RenderSection$ResortTransparencyTask lastResortTransparencyTask;
   private AABB bb;
   private volatile long sectionNode;
   private final BlockPos$MutableBlockPos renderOrigin;
   private long uploadedTime;

   public SectionRenderDispatcher$RenderSection(final SectionRenderDispatcher this$0, final int index, final long sectionNode) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.sectionMesh = new AtomicReference<>(CompiledSectionMesh.UNCOMPILED);
      this.sectionNode = SectionPos.asLong(-1, -1, -1);
      this.renderOrigin = new BlockPos$MutableBlockPos(-1, -1, -1);
      this.index = index;
      this.setSectionNode(sectionNode);
   }

   public float getVisibility(final long now, final long fadeDuration) {
      long elapsed = now - this.uploadedTime;
      return elapsed >= fadeDuration ? 1.0F : (float)elapsed / (float)fadeDuration;
   }

   public AABB getBoundingBox() {
      return this.bb;
   }

   @Override
   public void setSectionNode(final long sectionNode) {
      this.reset();
      this.sectionNode = sectionNode;
      int x = SectionPos.sectionToBlockCoord(SectionPos.x(sectionNode));
      int y = SectionPos.sectionToBlockCoord(SectionPos.y(sectionNode));
      int z = SectionPos.sectionToBlockCoord(SectionPos.z(sectionNode));
      this.renderOrigin.set(x, y, z);
      this.bb = new AABB(x, y, z, x + 16, y + 16, z + 16);
   }

   public SectionMesh getSectionMesh() {
      return this.sectionMesh.get();
   }

   public void reset() {
      this.cancelTasks();
      SectionMesh mesh = this.sectionMesh.getAndSet(CompiledSectionMesh.UNCOMPILED);
      this.this$0.copyLock.lock();

      try {
         this.releaseSectionMesh(mesh);
      } finally {
         this.this$0.copyLock.unlock();
      }

      this.uploadedTime = 0L;
   }

   public BlockPos getRenderOrigin() {
      return this.renderOrigin;
   }

   @Override
   public long getSectionNode() {
      return this.sectionNode;
   }

   public long getNeighborSectionNode(final Direction direction) {
      return SectionPos.offset(this.sectionNode, direction);
   }

   public void resortTransparency() {
      if (this.getSectionMesh() instanceof CompiledSectionMesh mesh) {
         this.lastResortTransparencyTask = new SectionRenderDispatcher$RenderSection$ResortTransparencyTask(this, mesh);
         this.this$0.schedule(this.lastResortTransparencyTask);
      }
   }

   public boolean hasTranslucentGeometry() {
      return this.getSectionMesh().hasTranslucentGeometry();
   }

   public boolean transparencyResortingScheduled() {
      return this.lastResortTransparencyTask != null && !this.lastResortTransparencyTask.isCompleted.get();
   }

   private void cancelTasks() {
      if (this.lastCompileTask != null) {
         this.lastCompileTask.cancel();
         this.lastCompileTask = null;
      }

      if (this.lastResortTransparencyTask != null) {
         this.lastResortTransparencyTask.cancel();
         this.lastResortTransparencyTask = null;
      }
   }

   private SectionRenderDispatcher$RenderSection$SectionTask createCompileTask(final RenderSectionRegion region) {
      this.cancelTasks();
      boolean isRecompile = this.sectionMesh.get() != CompiledSectionMesh.UNCOMPILED;
      this.lastCompileTask = new SectionRenderDispatcher$RenderSection$CompileTask(this, region, isRecompile);
      return this.lastCompileTask;
   }

   public void compileAsync(final RenderSectionRegion region) {
      SectionRenderDispatcher$RenderSection$SectionTask task = this.createCompileTask(region);
      this.this$0.schedule(task);
   }

   public void compileSync(final RenderSectionRegion region) {
      SectionRenderDispatcher$RenderSection$SectionTask task = this.createCompileTask(region);
      task.doTask(this.this$0.fixedBuffers);
   }

   private SectionMesh setSectionMesh(final SectionMesh sectionMesh) {
      SectionMesh oldMesh = this.sectionMesh.getAndSet(sectionMesh);
      this.this$0.onSectionMeshUpdate.accept(this);
      this.updateUploadTime();
      return oldMesh;
   }

   public void updateUploadTime() {
      if (this.uploadedTime == 0L) {
         this.uploadedTime = Util.getMillis();
      }
   }

   private void releaseSectionMesh(final SectionMesh oldMesh) {
      oldMesh.close();

      for (SectionRenderDispatcher$SectionUberBuffers buffers : this.this$0.chunkUberBuffers.values()) {
         buffers.vertexBuffer.removeAllocation(oldMesh);
         buffers.indexBuffer.removeAllocation(oldMesh);
      }
   }

   private VertexSorting createVertexSorting(final SectionPos sectionPos, final Vec3 cameraPos) {
      return VertexSorting.byDistance(
         (float)(cameraPos.x - sectionPos.minBlockX()), (float)(cameraPos.y - sectionPos.minBlockY()), (float)(cameraPos.z - sectionPos.minBlockZ())
      );
   }

   private void checkSectionMesh(final CompiledSectionMesh compiledSectionMesh) {
      boolean allBuffersUpdated = true;

      for (ChunkSectionLayer layer : ChunkSectionLayer.values()) {
         SectionMesh$SectionDraw draw = compiledSectionMesh.getSectionDraw(layer);
         if (draw != null) {
            allBuffersUpdated &= compiledSectionMesh.isIndexBufferUploaded(layer);
            allBuffersUpdated &= compiledSectionMesh.isVertexBufferUploaded(layer);
         }
      }

      if (allBuffersUpdated && this.sectionMesh.get() != compiledSectionMesh) {
         SectionMesh oldMesh = this.setSectionMesh(compiledSectionMesh);
         this.releaseSectionMesh(oldMesh);
      }
   }

   private void vertexBufferUploadCallback(final CompiledSectionMesh sectionMesh, final ChunkSectionLayer layer) {
      sectionMesh.setVertexBufferUploaded(layer);
      this.checkSectionMesh(sectionMesh);
   }

   private void indexBufferUploadCallback(final CompiledSectionMesh sectionMesh, final ChunkSectionLayer layer, final boolean sortedIndexBuffer) {
      sectionMesh.setIndexBufferUploaded(layer);
      if (!sortedIndexBuffer) {
         this.checkSectionMesh(sectionMesh);
      }
   }

   private boolean addSectionBuffersToUberBuffer(
      final ChunkSectionLayer layer, final CompiledSectionMesh key, @Nullable final ByteBuffer vertexBuffer, @Nullable final ByteBuffer indexBuffer
   ) {
      boolean success = true;
      this.this$0.copyLock.lock();

      try {
         SectionMesh$SectionDraw draw = key.getSectionDraw(layer);
         if (draw != null) {
            SectionRenderDispatcher$SectionUberBuffers sectionBuffers = this.this$0.chunkUberBuffers.get(layer);
            if (!$assertionsDisabled && sectionBuffers == null) {
               throw new AssertionError();
            }

            if (vertexBuffer != null) {
               UberGpuBuffer$UploadCallback<CompiledSectionMesh> callback = mesh -> this.vertexBufferUploadCallback(mesh, layer);
               success &= sectionBuffers.vertexBuffer.addAllocation(key, callback, vertexBuffer);
            }

            if (indexBuffer != null) {
               boolean sortedIndexBuffer = vertexBuffer == null;
               UberGpuBuffer$UploadCallback<CompiledSectionMesh> callback = mesh -> this.indexBufferUploadCallback(mesh, layer, sortedIndexBuffer);
               success &= sectionBuffers.indexBuffer.addAllocation(key, callback, indexBuffer);
            } else {
               key.setIndexBufferUploaded(layer);
            }
         }

         if (!success && RenderSystem.isOnRenderThread()) {
            this.this$0.uploadTerrainBuffersToGpu();
         }
      } finally {
         this.this$0.copyLock.unlock();
      }

      return success;
   }
}
