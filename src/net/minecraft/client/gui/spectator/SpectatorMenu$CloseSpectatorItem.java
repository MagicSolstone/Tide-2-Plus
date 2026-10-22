package net.minecraft.client.gui.spectator;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;

@Environment(EnvType.CLIENT)
class SpectatorMenu$CloseSpectatorItem implements SpectatorMenuItem {
   private SpectatorMenu$CloseSpectatorItem() {
   }

   @Override
   public void selectItem(final SpectatorMenu menu) {
      menu.exit();
   }

   @Override
   public Component getName() {
      return SpectatorMenu.CLOSE_MENU_TEXT;
   }

   @Override
   public void extractIcon(final GuiGraphicsExtractor graphics, final float brightness, final float alpha) {
      graphics.blitSprite(
         RenderPipelines.GUI_TEXTURED, SpectatorMenu.CLOSE_SPRITE, 0, 0, 16, 16, ARGB.colorFromFloat(alpha, brightness, brightness, brightness)
      );
   }

   @Override
   public boolean isEnabled() {
      return true;
   }
}
