package net.minecraft.client.renderer;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher$RenderSection;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;

@Environment(EnvType.CLIENT)
final class Octree$Leaf implements Octree$Node {
   private final SectionRenderDispatcher$RenderSection section;

   private Octree$Leaf(final Octree this$0, final SectionRenderDispatcher$RenderSection section) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.section = section;
   }

   @Override
   public void visitNodes(
      final Octree$OctreeVisitor visitor, final boolean skipFrustumCheck, final Frustum frustum, final int depth, final int closeDistance, boolean isClose
   ) {
      AABB boundingBox = this.section.getBoundingBox();
      if (skipFrustumCheck || frustum.isVisible(this.getSection().getBoundingBox())) {
         isClose = isClose
            && this.this$0.isClose(boundingBox.minX, boundingBox.minY, boundingBox.minZ, boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ, closeDistance);
         visitor.visit(this, skipFrustumCheck, depth, isClose);
      }
   }

   @Override
   public SectionRenderDispatcher$RenderSection getSection() {
      return this.section;
   }

   @Override
   public AABB getAABB() {
      return this.section.getBoundingBox();
   }
}
