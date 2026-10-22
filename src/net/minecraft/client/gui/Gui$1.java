package net.minecraft.client.gui;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.ActiveArea;
import net.minecraft.client.gui.font.EmptyArea;
import net.minecraft.client.gui.font.TextRenderable$Styled;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.ColoredRectangleRenderState;
import net.minecraft.client.renderer.state.gui.GuiTextRenderState;
import net.minecraft.network.chat.Style;
import net.minecraft.util.ARGB;

@Environment(EnvType.CLIENT)
class Gui$1 implements Font$GlyphVisitor {
   private int index;

   Gui$1(final Gui this$0, final GuiTextRenderState val$text) {
      this.val$text = val$textx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public void acceptGlyph(final TextRenderable$Styled glyph) {
      this.renderDebugMarkers(glyph, false);
   }

   @Override
   public void acceptEmptyArea(final EmptyArea empty) {
      this.renderDebugMarkers(empty, true);
   }

   private void renderDebugMarkers(final ActiveArea glyph, final boolean isEmpty) {
      int intensity = (isEmpty ? 128 : 255) - (this.index++ & 1) * 64;
      Style style = glyph.style();
      int red = style.getClickEvent() != null ? intensity : 0;
      int green = style.getHoverEvent() != null ? intensity : 0;
      int blue = red != 0 && green != 0 ? 0 : intensity;
      int color = ARGB.color(128, red, green, blue);
      this.this$0
         .guiRenderState
         .addGuiElement(
            new ColoredRectangleRenderState(
               RenderPipelines.GUI,
               TextureSetup.noTexture(),
               this.val$text.pose,
               (int)glyph.activeLeft(),
               (int)glyph.activeTop(),
               (int)glyph.activeRight(),
               (int)glyph.activeBottom(),
               color,
               color,
               this.val$text.scissor
            )
         );
   }
}
