package net.minecraft.server;

import net.minecraft.network.chat.Component;

public enum MinecraftServer$MultiplayerScope {
   OFF("off"),
   LAN("lan");

   private final Component translatable;
   private final Component tooltip;

   private MinecraftServer$MultiplayerScope(final String key) {
      this.translatable = Component.translatable("menu.multiplayerOptions.network." + key);
      this.tooltip = Component.translatable("menu.multiplayerOptions.network." + key + ".tooltip");
   }

   public Component getDisplayName() {
      return this.translatable;
   }

   public Component getTooltip() {
      return this.tooltip;
   }
}
