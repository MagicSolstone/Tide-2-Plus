package net.minecraft.client.gui.render;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font$GlyphVisitor;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.state.gui.GlyphRenderState;
import org.joml.Matrix3x2fc;

@Environment(EnvType.CLIENT)
class GuiRenderer$1 implements Font$GlyphVisitor {
   GuiRenderer$1(final GuiRenderer this$0, final Matrix3x2fc val$pose, final ScreenRectangle val$scissor) {
      this.val$pose = val$posex;
      this.val$scissor = val$scissorx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public void acceptRenderable(final TextRenderable renderable) {
      this.this$0.renderState.addGlyphToCurrentLayer(new GlyphRenderState(this.val$pose, renderable, this.val$scissor));
   }
}
