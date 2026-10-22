package net.minecraft.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.EmptyArea;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.client.gui.font.TextRenderable$Styled;

@Environment(EnvType.CLIENT)
public interface Font$GlyphVisitor {
   default void acceptGlyph(final TextRenderable$Styled glyph) {
      this.acceptRenderable(glyph);
   }

   default void acceptEffect(final TextRenderable effect) {
      this.acceptRenderable(effect);
   }

   default void acceptRenderable(final TextRenderable renderable) {
   }

   default void acceptEmptyArea(final EmptyArea empty) {
   }
}
