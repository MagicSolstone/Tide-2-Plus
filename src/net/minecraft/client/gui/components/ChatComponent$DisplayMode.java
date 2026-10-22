package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum ChatComponent$DisplayMode {
   BACKGROUND(false, false),
   FOREGROUND(true, false),
   FOREGROUND_RESTRICTED(true, true);

   public final boolean foreground;
   public final boolean showRestrictedPrompt;

   private ChatComponent$DisplayMode(final boolean foreground, final boolean showRestrictedPrompt) {
      this.foreground = foreground;
      this.showRestrictedPrompt = showRestrictedPrompt;
   }
}
