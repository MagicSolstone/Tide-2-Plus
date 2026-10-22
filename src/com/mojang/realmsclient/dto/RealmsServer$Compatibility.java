package com.mojang.realmsclient.dto;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum RealmsServer$Compatibility {
   UNVERIFIABLE,
   INCOMPATIBLE,
   RELEASE_TYPE_INCOMPATIBLE,
   NEEDS_DOWNGRADE,
   NEEDS_UPGRADE,
   COMPATIBLE;

   public boolean isCompatible() {
      return this == COMPATIBLE;
   }

   public boolean needsUpgrade() {
      return this == NEEDS_UPGRADE;
   }

   public boolean needsDowngrade() {
      return this == NEEDS_DOWNGRADE;
   }
}
