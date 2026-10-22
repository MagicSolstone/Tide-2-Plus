package net.minecraft.client.renderer.chunk;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.ByteBufferBuilder$Result;
import com.mojang.blaze3d.vertex.MeshData$SortState;
import com.mojang.blaze3d.vertex.VertexSorting;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.core.SectionPos;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
class SectionRenderDispatcher$RenderSection$ResortTransparencyTask extends SectionRenderDispatcher$RenderSection$SectionTask {
   private final CompiledSectionMesh compiledSectionMesh;

   public SectionRenderDispatcher$RenderSection$ResortTransparencyTask(
      final SectionRenderDispatcher$RenderSection this$1, final CompiledSectionMesh compiledSectionMesh
   ) {
      Objects.requireNonNull(this$1x);
      this.this$1 = this$1x;
      super(this$1x, true);
      this.compiledSectionMesh = compiledSectionMesh;
   }

   @Override
   public SectionRenderDispatcher$RenderSection$SectionTask$SectionTaskResult doTask(final SectionBufferBuilderPack buffers) {
      if (this.isCancelled.get()) {
         return SectionRenderDispatcher$RenderSection$SectionTask$SectionTaskResult.CANCELLED;
      } else {
         MeshData$SortState state = this.compiledSectionMesh.getTransparencyState();
         if (state != null && !this.compiledSectionMesh.isEmpty(ChunkSectionLayer.TRANSLUCENT)) {
            Vec3 cameraPos = this.this$1.this$0.cameraPosition.get();
            long sectionNode = this.this$1.sectionNode;
            VertexSorting vertexSorting = this.this$1.createVertexSorting(SectionPos.of(sectionNode), cameraPos);
            TranslucencyPointOfView translucencyPointOfView = TranslucencyPointOfView.of(cameraPos, sectionNode);
            if (!this.compiledSectionMesh.isDifferentPointOfView(translucencyPointOfView) && !translucencyPointOfView.isAxisAligned()) {
               return SectionRenderDispatcher$RenderSection$SectionTask$SectionTaskResult.CANCELLED;
            } else {
               ByteBufferBuilder$Result indexBuffer = state.buildSortedIndexBuffer(buffers.buffer(ChunkSectionLayer.TRANSLUCENT), vertexSorting);
               if (indexBuffer == null) {
                  return SectionRenderDispatcher$RenderSection$SectionTask$SectionTaskResult.CANCELLED;
               } else {
                  boolean success = false;

                  while (!success) {
                     if (this.isCancelled.get()) {
                        indexBuffer.close();
                        return SectionRenderDispatcher$RenderSection$SectionTask$SectionTaskResult.CANCELLED;
                     }

                     success = this.this$1
                        .addSectionBuffersToUberBuffer(ChunkSectionLayer.TRANSLUCENT, this.compiledSectionMesh, null, indexBuffer.byteBuffer());
                     if (!success && !RenderSystem.isOnRenderThread()) {
                        Thread.onSpinWait();
                     }
                  }

                  indexBuffer.close();
                  this.compiledSectionMesh.setTranslucencyPointOfView(translucencyPointOfView);
                  return SectionRenderDispatcher$RenderSection$SectionTask$SectionTaskResult.SUCCESSFUL;
               }
            }
         } else {
            return SectionRenderDispatcher$RenderSection$SectionTask$SectionTaskResult.CANCELLED;
         }
      }
   }

   @Override
   public void cancel() {
      this.isCancelled.set(true);
   }
}
