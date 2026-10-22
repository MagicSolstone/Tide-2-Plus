package net.minecraft.client.renderer;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher$RenderSection;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;

@Environment(EnvType.CLIENT)
class Octree$Branch implements Octree$Node {
   private final Octree$Node[] nodes;
   private final BoundingBox boundingBox;
   private final int bbCenterX;
   private final int bbCenterY;
   private final int bbCenterZ;
   private final Octree$AxisSorting sorting;
   private final boolean cameraXDiffNegative;
   private final boolean cameraYDiffNegative;
   private final boolean cameraZDiffNegative;

   public Octree$Branch(final Octree this$0, final BoundingBox boundingBox) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.nodes = new Octree$Node[8];
      this.boundingBox = boundingBox;
      this.bbCenterX = this.boundingBox.minX() + this.boundingBox.getXSpan() / 2;
      this.bbCenterY = this.boundingBox.minY() + this.boundingBox.getYSpan() / 2;
      this.bbCenterZ = this.boundingBox.minZ() + this.boundingBox.getZSpan() / 2;
      int cameraXDiff = this$0x.cameraSectionCenter.getX() - this.bbCenterX;
      int cameraYDiff = this$0x.cameraSectionCenter.getY() - this.bbCenterY;
      int cameraZDiff = this$0x.cameraSectionCenter.getZ() - this.bbCenterZ;
      this.sorting = Octree$AxisSorting.getAxisSorting(Math.abs(cameraXDiff), Math.abs(cameraYDiff), Math.abs(cameraZDiff));
      this.cameraXDiffNegative = cameraXDiff < 0;
      this.cameraYDiffNegative = cameraYDiff < 0;
      this.cameraZDiffNegative = cameraZDiff < 0;
   }

   public boolean add(final SectionRenderDispatcher$RenderSection section) {
      long sectionNode = section.getSectionNode();
      boolean sectionXDiffNegative = SectionPos.sectionToBlockCoord(SectionPos.x(sectionNode)) - this.bbCenterX < 0;
      boolean sectionYDiffNegative = SectionPos.sectionToBlockCoord(SectionPos.y(sectionNode)) - this.bbCenterY < 0;
      boolean sectionZDiffNegative = SectionPos.sectionToBlockCoord(SectionPos.z(sectionNode)) - this.bbCenterZ < 0;
      boolean xDiffsOppositeSides = sectionXDiffNegative != this.cameraXDiffNegative;
      boolean yDiffsOppositeSides = sectionYDiffNegative != this.cameraYDiffNegative;
      boolean zDiffsOppositeSides = sectionZDiffNegative != this.cameraZDiffNegative;
      int nodeIndex = getNodeIndex(this.sorting, xDiffsOppositeSides, yDiffsOppositeSides, zDiffsOppositeSides);
      if (this.areChildrenLeaves()) {
         boolean alreadyExisted = this.nodes[nodeIndex] != null;
         this.nodes[nodeIndex] = new Octree$Leaf(this.this$0, section);
         return !alreadyExisted;
      } else if (this.nodes[nodeIndex] != null) {
         Octree$Branch branch = (Octree$Branch)this.nodes[nodeIndex];
         return branch.add(section);
      } else {
         BoundingBox childBoundingBox = this.createChildBoundingBox(sectionXDiffNegative, sectionYDiffNegative, sectionZDiffNegative);
         Octree$Branch branch = new Octree$Branch(this.this$0, childBoundingBox);
         this.nodes[nodeIndex] = branch;
         return branch.add(section);
      }
   }

   private static int getNodeIndex(
      final Octree$AxisSorting sorting, final boolean xDiffsOppositeSides, final boolean yDiffsOppositeSides, final boolean zDiffsOppositeSides
   ) {
      int index = 0;
      if (xDiffsOppositeSides) {
         index += sorting.xShift;
      }

      if (yDiffsOppositeSides) {
         index += sorting.yShift;
      }

      if (zDiffsOppositeSides) {
         index += sorting.zShift;
      }

      return index;
   }

   private boolean areChildrenLeaves() {
      return this.boundingBox.getXSpan() == 32;
   }

   private BoundingBox createChildBoundingBox(final boolean sectionXDiffNegative, final boolean sectionYDiffNegative, final boolean sectionZDiffNegative) {
      int minX;
      int maxX;
      if (sectionXDiffNegative) {
         minX = this.boundingBox.minX();
         maxX = this.bbCenterX - 1;
      } else {
         minX = this.bbCenterX;
         maxX = this.boundingBox.maxX();
      }

      int minY;
      int maxY;
      if (sectionYDiffNegative) {
         minY = this.boundingBox.minY();
         maxY = this.bbCenterY - 1;
      } else {
         minY = this.bbCenterY;
         maxY = this.boundingBox.maxY();
      }

      int minZ;
      int maxZ;
      if (sectionZDiffNegative) {
         minZ = this.boundingBox.minZ();
         maxZ = this.bbCenterZ - 1;
      } else {
         minZ = this.bbCenterZ;
         maxZ = this.boundingBox.maxZ();
      }

      return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
   }

   @Override
   public void visitNodes(
      final Octree$OctreeVisitor visitor, boolean skipFrustumCheck, final Frustum frustum, final int depth, final int closeDistance, boolean isClose
   ) {
      boolean isVisible = skipFrustumCheck;
      if (!skipFrustumCheck) {
         int checkResult = frustum.cubeInFrustum(this.boundingBox);
         skipFrustumCheck = checkResult == -2;
         isVisible = checkResult == -2 || checkResult == -1;
      }

      if (isVisible) {
         isClose = isClose
            && this.this$0
               .isClose(
                  (double)this.boundingBox.minX(),
                  (double)this.boundingBox.minY(),
                  (double)this.boundingBox.minZ(),
                  (double)this.boundingBox.maxX(),
                  (double)this.boundingBox.maxY(),
                  (double)this.boundingBox.maxZ(),
                  closeDistance
               );
         visitor.visit(this, skipFrustumCheck, depth, isClose);

         for (Octree$Node node : this.nodes) {
            if (node != null) {
               node.visitNodes(visitor, skipFrustumCheck, frustum, depth + 1, closeDistance, isClose);
            }
         }
      }
   }

   @Override
   public SectionRenderDispatcher$RenderSection getSection() {
      return null;
   }

   @Override
   public AABB getAABB() {
      return new AABB(
         this.boundingBox.minX(),
         this.boundingBox.minY(),
         this.boundingBox.minZ(),
         this.boundingBox.maxX() + 1,
         this.boundingBox.maxY() + 1,
         this.boundingBox.maxZ() + 1
      );
   }
}
