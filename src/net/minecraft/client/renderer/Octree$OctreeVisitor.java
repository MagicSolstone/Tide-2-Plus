package net.minecraft.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface Octree$OctreeVisitor {
   void visit(final Octree$Node node, final boolean fullyVisible, int depth, boolean isClose);
}
