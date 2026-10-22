package net.minecraft.client.gui.screens.inventory;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
abstract class BeaconScreen$BeaconSpriteScreenButton extends BeaconScreen$BeaconScreenButton {
   private final Identifier sprite;

   protected BeaconScreen$BeaconSpriteScreenButton(final int x, final int y, final Identifier sprite, final Component label) {
      super(x, y, label);
      this.setTooltip(Tooltip.create(label));
      this.sprite = sprite;
   }

   @Override
   protected void extractIcon(final GuiGraphicsExtractor graphics) {
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, this.getX() + 2, this.getY() + 2, 18, 18);
   }
}
