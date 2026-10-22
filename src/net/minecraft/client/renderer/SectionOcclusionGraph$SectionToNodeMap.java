package net.minecraft.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher$RenderSection;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class SectionOcclusionGraph$SectionToNodeMap {
   private final SectionOcclusionGraph$Node[] nodes;

   private SectionOcclusionGraph$SectionToNodeMap(final int sectionCount) {
      this.nodes = new SectionOcclusionGraph$Node[sectionCount];
   }

   public void put(final SectionRenderDispatcher$RenderSection renderSection, final SectionOcclusionGraph$Node node) {
      this.nodes[renderSection.index] = node;
   }

   @Nullable
   public SectionOcclusionGraph$Node get(final SectionRenderDispatcher$RenderSection renderSection) {
      int index = renderSection.index;
      return index >= 0 && index < this.nodes.length ? this.nodes[index] : null;
   }
}
