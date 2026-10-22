package net.minecraft.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher$RenderSection;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

@Environment(EnvType.CLIENT)
public class Octree {
   private final Octree$Branch root;
   private final BlockPos cameraSectionCenter;

   public Octree(final SectionPos cameraSection, final int renderDistance, final int sectionsPerChunk, final int minBlockY) {
      int visibleAreaDiameterInSections = renderDistance * 2 + 1;
      int boundingBoxSizeInSections = Mth.smallestEncompassingPowerOfTwo(visibleAreaDiameterInSections);
      int distanceToBBEdgeInBlocks = renderDistance * 16;
      BlockPos cameraSectionOrigin = cameraSection.origin();
      this.cameraSectionCenter = cameraSection.center();
      int minX = cameraSectionOrigin.getX() - distanceToBBEdgeInBlocks;
      int maxX = minX + boundingBoxSizeInSections * 16 - 1;
      int minY = boundingBoxSizeInSections >= sectionsPerChunk ? minBlockY : cameraSectionOrigin.getY() - distanceToBBEdgeInBlocks;
      int maxY = minY + boundingBoxSizeInSections * 16 - 1;
      int minZ = cameraSectionOrigin.getZ() - distanceToBBEdgeInBlocks;
      int maxZ = minZ + boundingBoxSizeInSections * 16 - 1;
      this.root = new Octree$Branch(this, new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ));
   }

   public boolean add(final SectionRenderDispatcher$RenderSection section) {
      return this.root.add(section);
   }

   public void visitNodes(final Octree$OctreeVisitor visitor, final Frustum frustum, final int closeDistance) {
      this.root.visitNodes(visitor, false, frustum, 0, closeDistance, true);
   }

   private boolean isClose(
      final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ, final int closeDistance
   ) {
      int cameraX = this.cameraSectionCenter.getX();
      int cameraY = this.cameraSectionCenter.getY();
      int cameraZ = this.cameraSectionCenter.getZ();
      return cameraX > minX - closeDistance
         && cameraX < maxX + closeDistance
         && cameraY > minY - closeDistance
         && cameraY < maxY + closeDistance
         && cameraZ > minZ - closeDistance
         && cameraZ < maxZ + closeDistance;
   }
}
