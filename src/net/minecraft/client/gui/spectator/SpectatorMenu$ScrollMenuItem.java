package net.minecraft.client.gui.spectator;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;

@Environment(EnvType.CLIENT)
class SpectatorMenu$ScrollMenuItem implements SpectatorMenuItem {
   private final int direction;
   private final boolean enabled;

   public SpectatorMenu$ScrollMenuItem(final int direction, final boolean enabled) {
      this.direction = direction;
      this.enabled = enabled;
   }

   @Override
   public void selectItem(final SpectatorMenu menu) {
      menu.page = menu.page + this.direction;
   }

   @Override
   public Component getName() {
      return this.direction < 0 ? SpectatorMenu.PREVIOUS_PAGE_TEXT : SpectatorMenu.NEXT_PAGE_TEXT;
   }

   @Override
   public void extractIcon(final GuiGraphicsExtractor graphics, final float brightness, final float alpha) {
      int color = ARGB.colorFromFloat(alpha, brightness, brightness, brightness);
      if (this.direction < 0) {
         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SpectatorMenu.SCROLL_LEFT_SPRITE, 0, 0, 16, 16, color);
      } else {
         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SpectatorMenu.SCROLL_RIGHT_SPRITE, 0, 0, 16, 16, color);
      }
   }

   @Override
   public boolean isEnabled() {
      return this.enabled;
   }
}
