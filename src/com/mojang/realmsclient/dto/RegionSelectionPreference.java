package com.mojang.realmsclient.dto;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum RegionSelectionPreference {
   AUTOMATIC_PLAYER(0, "realms.configuration.region_preference.automatic_player"),
   AUTOMATIC_OWNER(1, "realms.configuration.region_preference.automatic_owner"),
   MANUAL(2, "");

   public static final RegionSelectionPreference DEFAULT_SELECTION = AUTOMATIC_PLAYER;
   public final int id;
   public final String translationKey;

   private RegionSelectionPreference(final int id, final String translationKey) {
      this.id = id;
      this.translationKey = translationKey;
   }
}
