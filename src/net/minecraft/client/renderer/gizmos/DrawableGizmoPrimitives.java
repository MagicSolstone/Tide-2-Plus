package net.minecraft.client.renderer.gizmos;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.gizmos.GizmoPrimitives;
import net.minecraft.gizmos.TextGizmo$Style;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class DrawableGizmoPrimitives implements GizmoPrimitives {
   private final DrawableGizmoPrimitives$Group opaque = new DrawableGizmoPrimitives$Group(true);
   private final DrawableGizmoPrimitives$Group translucent = new DrawableGizmoPrimitives$Group(false);
   private boolean isEmpty = true;

   private DrawableGizmoPrimitives$Group getGroup(final int color) {
      return ARGB.alpha(color) < 255 ? this.translucent : this.opaque;
   }

   @Override
   public void addPoint(final Vec3 pos, final int color, final float size) {
      this.getGroup(color).points.add(new DrawableGizmoPrimitives$Point(pos, color, size));
      this.isEmpty = false;
   }

   @Override
   public void addLine(final Vec3 start, final Vec3 end, final int color, final float width) {
      this.getGroup(color).lines.add(new DrawableGizmoPrimitives$Line(start, end, color, width));
      this.isEmpty = false;
   }

   @Override
   public void addTriangleFan(final Vec3[] points, final int color) {
      this.getGroup(color).triangleFans.add(new DrawableGizmoPrimitives$TriangleFan(points, color));
      this.isEmpty = false;
   }

   @Override
   public void addQuad(final Vec3 a, final Vec3 b, final Vec3 c, final Vec3 d, final int color) {
      this.getGroup(color).quads.add(new DrawableGizmoPrimitives$Quad(a, b, c, d, color));
      this.isEmpty = false;
   }

   @Override
   public void addText(final Vec3 pos, final String text, final TextGizmo$Style style) {
      this.getGroup(style.color()).texts.add(new DrawableGizmoPrimitives$Text(pos, text, style));
      this.isEmpty = false;
   }

   public void submit(final SubmitNodeCollector submitNodeCollector, final CameraRenderState cameraRenderState, final boolean onTop) {
      if (!this.isEmpty) {
         submitNodeCollector.submitGizmoPrimitives(this.opaque, cameraRenderState, onTop);
         submitNodeCollector.submitGizmoPrimitives(this.translucent, cameraRenderState, onTop);
      }
   }

   public boolean isEmpty() {
      return this.isEmpty;
   }
}
