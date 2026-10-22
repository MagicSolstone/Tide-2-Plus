package com.mojang.realmsclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button$OnPress;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
class RealmsMainScreen$CrossButton extends ImageButton {
   private static final WidgetSprites SPRITES = new WidgetSprites(
      Identifier.withDefaultNamespace("widget/cross_button"), Identifier.withDefaultNamespace("widget/cross_button_highlighted")
   );

   protected RealmsMainScreen$CrossButton(final Button$OnPress onPress, final Component tooltip) {
      super(0, 0, 14, 14, SPRITES, onPress);
      this.setTooltip(Tooltip.create(tooltip));
   }
}
