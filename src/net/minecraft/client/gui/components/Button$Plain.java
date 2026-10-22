package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.GuiGraphicsExtractor$HoveredTextEffects;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class Button$Plain extends Button {
   protected Button$Plain(
      final int x,
      final int y,
      final int width,
      final int height,
      final Component message,
      final Button$OnPress onPress,
      final Button$CreateNarration createNarration
   ) {
      super(x, y, width, height, message, onPress, createNarration);
   }

   @Override
   protected void extractContents(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      this.extractDefaultSprite(graphics);
      this.extractDefaultLabel(graphics.textRendererForWidget(this, GuiGraphicsExtractor$HoveredTextEffects.NONE));
   }
}
