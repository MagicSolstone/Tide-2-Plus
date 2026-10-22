package net.minecraft.client.renderer.chunk;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.MeshData;
import java.util.Objects;
import java.util.Map.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Util;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.Zone;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
class SectionRenderDispatcher$RenderSection$CompileTask extends SectionRenderDispatcher$RenderSection$SectionTask {
   private final RenderSectionRegion region;
   private final long startTimeNs;

   public SectionRenderDispatcher$RenderSection$CompileTask(
      final SectionRenderDispatcher$RenderSection this$1, final RenderSectionRegion region, final boolean isRecompile
   ) {
      Objects.requireNonNull(this$1x);
      this.this$1 = this$1x;
      super(this$1x, isRecompile);
      this.region = region;
      this.startTimeNs = Util.getNanos();
   }

   @Override
   public SectionRenderDispatcher$RenderSection$SectionTask$SectionTaskResult doTask(final SectionBufferBuilderPack buffers) {
      if (this.isCancelled.get()) {
         return SectionRenderDispatcher$RenderSection$SectionTask$SectionTaskResult.CANCELLED;
      } else {
         long sectionNode = this.this$1.sectionNode;
         SectionPos sectionPos = SectionPos.of(sectionNode);
         if (this.isCancelled.get()) {
            return SectionRenderDispatcher$RenderSection$SectionTask$SectionTaskResult.CANCELLED;
         } else {
            Vec3 cameraPos = this.this$1.this$0.cameraPosition.get();

            SectionCompiler$Results results;
            try (Zone ignored = Profiler.get().zone("Compile Section")) {
               results = this.this$1.this$0.sectionCompiler.compile(sectionPos, this.region, this.this$1.createVertexSorting(sectionPos, cameraPos), buffers);
            }

            TranslucencyPointOfView translucencyPointOfView = TranslucencyPointOfView.of(cameraPos, sectionNode);
            CompiledSectionMesh compiledSectionMesh = new CompiledSectionMesh(translucencyPointOfView, results, this.startTimeNs);
            if (results.renderedLayers.isEmpty()) {
               SectionMesh oldMesh = this.this$1
                  .setSectionMesh((SectionMesh)(results.blockEntities.isEmpty() ? CompiledSectionMesh.EMPTY : compiledSectionMesh));
               this.this$1.this$0.copyLock.lock();

               try {
                  this.this$1.releaseSectionMesh(oldMesh);
               } finally {
                  this.this$1.this$0.copyLock.unlock();
               }

               return SectionRenderDispatcher$RenderSection$SectionTask$SectionTaskResult.SUCCESSFUL;
            } else {
               for (Entry<ChunkSectionLayer, MeshData> entry : results.renderedLayers.entrySet()) {
                  MeshData meshData = entry.getValue();
                  boolean success = false;

                  while (!success) {
                     if (this.isCancelled.get()) {
                        results.release();
                        this.this$1.this$0.copyLock.lock();

                        try {
                           this.this$1.releaseSectionMesh(compiledSectionMesh);
                        } finally {
                           this.this$1.this$0.copyLock.unlock();
                        }

                        return SectionRenderDispatcher$RenderSection$SectionTask$SectionTaskResult.CANCELLED;
                     }

                     success = this.this$1.addSectionBuffersToUberBuffer(entry.getKey(), compiledSectionMesh, meshData.vertexBuffer(), meshData.indexBuffer());
                     if (!success && !RenderSystem.isOnRenderThread()) {
                        Thread.onSpinWait();
                     }
                  }

                  meshData.close();
               }

               return SectionRenderDispatcher$RenderSection$SectionTask$SectionTaskResult.SUCCESSFUL;
            }
         }
      }
   }

   @Override
   public void cancel() {
      this.isCancelled.compareAndSet(false, true);
   }
}
