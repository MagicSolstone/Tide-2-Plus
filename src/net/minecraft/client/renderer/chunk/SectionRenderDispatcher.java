package net.minecraft.client.renderer.chunk;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.StagingBuffer;
import com.mojang.blaze3d.vertex.StagingBuffer$Uploader;
import com.mojang.blaze3d.vertex.TlsfAllocator$Allocation;
import com.mojang.blaze3d.vertex.UberGpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.device.GpuDevice;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.CrashReport;
import net.minecraft.TracingExecutor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.client.renderer.SectionBufferBuilderPool;
import net.minecraft.util.Util;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class SectionRenderDispatcher {
   public static final int NEARBY_SECTION_DISTANCE_IN_BLOCKS = 32;
   private final SectionTaskDynamicQueue queue = new SectionTaskDynamicQueue();
   private final SectionBufferBuilderPack fixedBuffers;
   private final SectionBufferBuilderPool bufferPool;
   private volatile boolean closed;
   private final TracingExecutor executor;
   private final Consumer<SectionRenderDispatcher$RenderSection> onSectionMeshUpdate;
   private final AtomicReference<Vec3> cameraPosition = new AtomicReference<>(Vec3.ZERO);
   private volatile SectionCompiler sectionCompiler;
   private final StagingBuffer stagingBuffer;
   private final Map<ChunkSectionLayer, SectionRenderDispatcher$SectionUberBuffers> chunkUberBuffers;
   private final ReentrantLock copyLock = new ReentrantLock();

   public SectionRenderDispatcher(
      final TracingExecutor executor,
      final RenderBuffers renderBuffers,
      final SectionCompiler sectionCompiler,
      final Consumer<SectionRenderDispatcher$RenderSection> onSectionMeshUpdate
   ) {
      this.onSectionMeshUpdate = onSectionMeshUpdate;
      this.fixedBuffers = renderBuffers.fixedBufferPack();
      this.bufferPool = renderBuffers.sectionBufferPool();
      this.executor = executor;
      this.sectionCompiler = sectionCompiler;
      int vertexBufferHeapSize = 134217728;
      int indexBufferHeapSize = 33554432;
      int stagingBufferSize = 102760448;
      GpuDevice gpuDevice = RenderSystem.getDevice();
      this.stagingBuffer = StagingBuffer.create("Chunk", gpuDevice, 102760448);
      this.chunkUberBuffers = Util.makeEnumMap(ChunkSectionLayer.class, layer -> {
         VertexFormat vertexFormat = layer.pipeline(false).getVertexFormatBinding(0);
         UberGpuBuffer<SectionMesh> vertexUberBuffer = new UberGpuBuffer<>(layer.label(), 32, 134217728, vertexFormat.getVertexSize(), this.stagingBuffer);
         UberGpuBuffer<SectionMesh> indexUberBuffer = new UberGpuBuffer<>(layer.label(), 64, 33554432, 8, this.stagingBuffer);
         return new SectionRenderDispatcher$SectionUberBuffers(vertexUberBuffer, indexUberBuffer);
      });
   }

   public void setCompiler(final SectionCompiler sectionCompiler) {
      this.sectionCompiler = sectionCompiler;
   }

   private void runTask() {
      if (!this.closed) {
         SectionRenderDispatcher$RenderSection$SectionTask task = this.queue.poll(this.cameraPosition.get());
         if (task != null && !task.isCompleted.get() && !task.isCancelled.get()) {
            try {
               SectionBufferBuilderPack buffer = Objects.requireNonNull(this.bufferPool.acquire());
               SectionRenderDispatcher$RenderSection$SectionTask$SectionTaskResult result = task.doTask(buffer);
               task.isCompleted.set(true);
               if (result == SectionRenderDispatcher$RenderSection$SectionTask$SectionTaskResult.SUCCESSFUL) {
                  buffer.clearAll();
               } else {
                  buffer.discardAll();
               }

               this.bufferPool.release(buffer);
               this.executor.execute(this::runTask);
            } catch (NullPointerException var4) {
               this.queue.add(task);
            } catch (Exception var5) {
               Minecraft.getInstance().delayCrash(CrashReport.forThrowable(var5, "Batching sections"));
            }
         }
      }
   }

   public void setCameraPosition(final Vec3 cameraPosition) {
      this.cameraPosition.set(cameraPosition);
   }

   @Nullable
   public SectionRenderDispatcher$RenderSectionBufferSlice getRenderSectionSlice(final SectionMesh sectionMesh, final ChunkSectionLayer layer) {
      SectionRenderDispatcher$SectionUberBuffers uberBuffers = this.chunkUberBuffers.get(layer);
      TlsfAllocator$Allocation vertexSlice = uberBuffers.vertexBuffer.getAllocation(sectionMesh);
      if (vertexSlice == null) {
         return null;
      } else {
         long vertexBufferOffset = vertexSlice.getOffsetFromHeap();
         TlsfAllocator$Allocation indexSlice = uberBuffers.indexBuffer.getAllocation(sectionMesh);
         long indexBufferOffset = 0L;
         GpuBuffer indexBuffer = null;
         if (indexSlice != null) {
            indexBufferOffset = indexSlice.getOffsetFromHeap();
            indexBuffer = uberBuffers.indexBuffer.getGpuBuffer(indexSlice);
         }

         return new SectionRenderDispatcher$RenderSectionBufferSlice(
            uberBuffers.vertexBuffer.getGpuBuffer(vertexSlice), vertexBufferOffset, indexBuffer, indexBufferOffset
         );
      }
   }

   public void lock() {
      this.copyLock.lock();
   }

   public void unlock() {
      this.copyLock.unlock();
   }

   public void uploadTerrainBuffersToGpu() {
      GpuDevice device = RenderSystem.getDevice();

      try (StagingBuffer$Uploader uploader = this.stagingBuffer.startUploading(device.createCommandEncoder())) {
         for (SectionRenderDispatcher$SectionUberBuffers buffers : this.chunkUberBuffers.values()) {
            boolean performedBufferResize = buffers.vertexBuffer.uploadStagedAllocations(device, uploader);
            buffers.indexBuffer.uploadStagedAllocations(device, uploader);
            if (performedBufferResize) {
               break;
            }
         }
      }
   }

   private void schedule(final SectionRenderDispatcher$RenderSection$SectionTask task) {
      if (!this.closed) {
         this.queue.add(task);
         this.executor.execute(this::runTask);
      }
   }

   public void clearCompileQueue() {
      this.queue.clear();
   }

   public boolean isQueueEmpty() {
      return this.queue.size() == 0;
   }

   public void dispose() {
      this.closed = true;
      this.clearCompileQueue();
      this.copyLock.lock();

      try {
         for (SectionRenderDispatcher$SectionUberBuffers buffers : this.chunkUberBuffers.values()) {
            buffers.vertexBuffer.close();
            buffers.indexBuffer.close();
         }

         this.stagingBuffer.close();
      } finally {
         this.copyLock.unlock();
      }
   }

   @VisibleForDebug
   public String getStats() {
      return String.format(Locale.ROOT, "pC: %03d, aB: %02d", this.queue.size(), this.bufferPool.getFreeBufferCount());
   }

   @VisibleForDebug
   public int getCompileQueueSize() {
      return this.queue.size();
   }

   @VisibleForDebug
   public int getFreeBufferCount() {
      return this.bufferPool.getFreeBufferCount();
   }
}
