package net.minecraft.client.gui.screens;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class DeathScreen$TitleConfirmScreen extends ConfirmScreen {
   public DeathScreen$TitleConfirmScreen(
      final BooleanConsumer callback, final Component title, final Component message, final Component yesButton, final Component noButton
   ) {
      super(callback, title, message, yesButton, noButton);
   }

   @Override
   public void extractBackground(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      DeathScreen.extractDeathBackground(graphics, this.width, this.height);
   }
}
