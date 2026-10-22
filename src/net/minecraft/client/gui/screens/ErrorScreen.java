package net.minecraft.client.gui.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class ErrorScreen extends Screen {
   private final Component messageText;
   private MultiLineLabel message;

   public ErrorScreen(final Component title, final Component messageText) {
      super(title);
      this.messageText = messageText;
   }

   @Override
   protected void init() {
      super.init();
      this.message = MultiLineLabel.create(this.font, this.messageText, this.width - 50);
      this.addRenderableWidget(
         Button.builder(CommonComponents.GUI_CANCEL, button -> this.minecraft.gui.setScreen(null)).bounds(this.width / 2 - 100, 140, 200, 20).build()
      );
   }

   @Override
   public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      super.extractRenderState(graphics, mouseX, mouseY, a);
      graphics.centeredText(this.font, this.title, this.width / 2, 90, -1);
      this.message.visitLines(TextAlignment.CENTER, this.width / 2, 110, 9, graphics.textRenderer());
   }

   @Override
   public void extractBackground(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      graphics.fillGradient(0, 0, this.width, this.height, -12574688, -11530224);
   }

   @Override
   public boolean shouldCloseOnEsc() {
      return false;
   }
}
