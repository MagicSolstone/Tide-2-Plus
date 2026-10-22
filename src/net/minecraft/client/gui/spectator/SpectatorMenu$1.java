package net.minecraft.client.gui.spectator;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
class SpectatorMenu$1 implements SpectatorMenuItem {
   @Override
   public void selectItem(final SpectatorMenu menu) {
   }

   @Override
   public Component getName() {
      return CommonComponents.EMPTY;
   }

   @Override
   public void extractIcon(final GuiGraphicsExtractor graphics, final float brightness, final float alpha) {
   }

   @Override
   public boolean isEnabled() {
      return false;
   }
}
