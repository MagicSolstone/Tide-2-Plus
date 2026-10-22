package net.minecraft.client.renderer.gizmos;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record DrawableGizmoPrimitives$Group(
   boolean opaque,
   List<DrawableGizmoPrimitives$Line> lines,
   List<DrawableGizmoPrimitives$Quad> quads,
   List<DrawableGizmoPrimitives$TriangleFan> triangleFans,
   List<DrawableGizmoPrimitives$Text> texts,
   List<DrawableGizmoPrimitives$Point> points
) {
   private DrawableGizmoPrimitives$Group(final boolean opaque) {
      this(opaque, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
   }
}
