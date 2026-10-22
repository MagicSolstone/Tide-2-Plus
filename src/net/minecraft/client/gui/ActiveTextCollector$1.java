package net.minecraft.client.gui;

import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.ActiveArea;
import net.minecraft.client.gui.font.EmptyArea;
import net.minecraft.client.gui.font.TextRenderable$Styled;

@Environment(EnvType.CLIENT)
class ActiveTextCollector$1 implements Font$GlyphVisitor {
   ActiveTextCollector$1(final float val$localMouseX, final float val$localMouseY, final Consumer val$output) {
      this.val$localMouseX = val$localMouseXx;
      this.val$localMouseY = val$localMouseYx;
      this.val$output = val$outputx;
   }

   @Override
   public void acceptGlyph(final TextRenderable$Styled glyph) {
      this.acceptActiveArea(glyph);
   }

   @Override
   public void acceptEmptyArea(final EmptyArea empty) {
      this.acceptActiveArea(empty);
   }

   private void acceptActiveArea(final ActiveArea glyph) {
      if (ActiveTextCollector.isPointInRectangle(
         this.val$localMouseX, this.val$localMouseY, glyph.activeLeft(), glyph.activeTop(), glyph.activeRight(), glyph.activeBottom()
      )) {
         this.val$output.accept(glyph.style());
      }
   }
}
