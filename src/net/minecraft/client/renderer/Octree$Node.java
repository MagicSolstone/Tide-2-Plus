package net.minecraft.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher$RenderSection;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;

@Environment(EnvType.CLIENT)
public interface Octree$Node {
   void visitNodes(Octree$OctreeVisitor visitor, boolean skipFrustumCheck, Frustum frustum, int depth, final int closeDistance, boolean isClose);

   SectionRenderDispatcher$RenderSection getSection();

   AABB getAABB();
}
