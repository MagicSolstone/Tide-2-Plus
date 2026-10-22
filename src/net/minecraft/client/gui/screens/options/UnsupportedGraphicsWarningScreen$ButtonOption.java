package net.minecraft.client.gui.screens.options;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button$OnPress;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public final class UnsupportedGraphicsWarningScreen$ButtonOption {
   private final Component message;
   private final Button$OnPress onPress;

   public UnsupportedGraphicsWarningScreen$ButtonOption(final Component message, final Button$OnPress onPress) {
      this.message = message;
      this.onPress = onPress;
   }
}
