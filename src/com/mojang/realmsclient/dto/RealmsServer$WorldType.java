package com.mojang.realmsclient.dto;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public enum RealmsServer$WorldType {
   NORMAL("normal"),
   MINIGAME("minigame"),
   ADVENTUREMAP("adventureMap"),
   EXPERIENCE("experience"),
   INSPIRATION("inspiration"),
   UNKNOWN("unknown");

   private static final String TRANSLATION_PREFIX = "mco.backup.entry.worldType.";
   private final Component displayName;

   private RealmsServer$WorldType(final String translationKey) {
      this.displayName = Component.translatable("mco.backup.entry.worldType." + translationKey);
   }

   public Component getDisplayName() {
      return this.displayName;
   }
}
