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
class TextFeatureRenderer$GlyphRenderer implements Font$GlyphVisitor {
   private final Matrix4f pose;
   private int lightCoords;
   private Font$DisplayMode displayMode;

   private TextFeatureRenderer$GlyphRenderer(final TextFeatureRenderer this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.pose = new Matrix4f();
      this.lightCoords = 15728880;
      this.displayMode = Font$DisplayMode.NORMAL;
   }

   @Override
   public void acceptRenderable(final TextRenderable renderable) {
      VertexConsumer builder = this.this$0.getVertexBuilder(renderable.renderType(this.displayMode));
      renderable.render(this.pose, builder, this.lightCoords, false);
   }
}
