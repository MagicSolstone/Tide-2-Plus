package net.minecraft.client.renderer.feature;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font$DisplayMode;
import net.minecraft.client.gui.Font$GlyphVisitor;
import net.minecraft.client.gui.font.TextRenderable;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
class GizmoFeatureRenderer$1 implements Font$GlyphVisitor {
   GizmoFeatureRenderer$1(final GizmoFeatureRenderer this$0, final Matrix4f val$pose) {
      this.val$pose = val$posex;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public void acceptRenderable(final TextRenderable renderable) {
      VertexConsumer buffer = this.this$0.getVertexBuilder(renderable.renderType(Font$DisplayMode.NORMAL));
      renderable.render(this.val$pose, buffer, 15728880, false);
   }
}
