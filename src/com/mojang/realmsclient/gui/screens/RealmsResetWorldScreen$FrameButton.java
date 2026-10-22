package com.mojang.realmsclient.gui.screens;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button$OnPress;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;

@Environment(EnvType.CLIENT)
class RealmsResetWorldScreen$FrameButton extends Button {
   private static final Identifier SLOT_FRAME_SPRITE = Identifier.withDefaultNamespace("widget/slot_frame");
   private static final int FRAME_SIZE = 60;
   private static final int FRAME_WIDTH = 2;
   private static final int IMAGE_SIZE = 56;
   private final Identifier image;

   private RealmsResetWorldScreen$FrameButton(
      final RealmsResetWorldScreen this$0, final Font font, final Component text, final Identifier image, final Button$OnPress onPress
   ) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(0, 0, 60, 60 + 9, text, onPress, DEFAULT_NARRATION);
      this.image = image;
   }

   @Override
   public void extractContents(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      boolean hoveredOrFocused = this.isHoveredOrFocused();
      int color = -1;
      if (hoveredOrFocused) {
         color = ARGB.colorFromFloat(1.0F, 0.56F, 0.56F, 0.56F);
      }

      int x = this.getX();
      int y = this.getY();
      graphics.blit(RenderPipelines.GUI_TEXTURED, this.image, x + 2, y + 2, 0.0F, 0.0F, 56, 56, 56, 56, 56, 56, color);
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_FRAME_SPRITE, x, y, 60, 60, color);
      int textColor = hoveredOrFocused ? -6250336 : -1;
      graphics.centeredText(RealmsResetWorldScreen.access$100(this.this$0), this.getMessage(), x + 28, y - 14, textColor);
   }
}
