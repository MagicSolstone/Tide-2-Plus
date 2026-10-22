package net.minecraft.client.renderer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher$RenderSection;

@Environment(EnvType.CLIENT)
record SectionOcclusionGraph$GraphState(
   SectionOcclusionGraph$GraphStorage storage, BlockingQueue<SectionRenderDispatcher$RenderSection> sectionsToPropagateFrom
) {
   private SectionOcclusionGraph$GraphState(final ViewArea viewArea) {
      this(new SectionOcclusionGraph$GraphStorage(viewArea), new LinkedBlockingQueue<>());
   }
}
