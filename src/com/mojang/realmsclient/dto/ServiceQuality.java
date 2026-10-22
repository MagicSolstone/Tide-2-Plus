package com.mojang.realmsclient.dto;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public enum ServiceQuality {
   GREAT(1, "icon/ping_5"),
   GOOD(2, "icon/ping_4"),
   OKAY(3, "icon/ping_3"),
   POOR(4, "icon/ping_2"),
   UNKNOWN(5, "icon/ping_unknown");

   private final int value;
   private final Identifier icon;

   private ServiceQuality(final int value, final String iconPath) {
      this.value = value;
      this.icon = Identifier.withDefaultNamespace(iconPath);
   }

   @Nullable
   public static ServiceQuality byValue(final int value) {
      for (ServiceQuality quality : values()) {
         if (quality.getValue() == value) {
            return quality;
         }
      }

      return null;
   }

   public int getValue() {
      return this.value;
   }

   public Identifier getIcon() {
      return this.icon;
   }
}
