package com.mojang.realmsclient;

import com.mojang.realmsclient.client.RealmsClient$Environment;
import com.mojang.realmsclient.dto.RealmsServer$Compatibility;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class RealmsMainScreen$2 {
   static {
      try {
         $SwitchMap$com$mojang$realmsclient$dto$RealmsServer$Compatibility[RealmsServer$Compatibility.COMPATIBLE.ordinal()] = 1;
      } catch (NoSuchFieldError var8) {
      }

      try {
         $SwitchMap$com$mojang$realmsclient$dto$RealmsServer$Compatibility[RealmsServer$Compatibility.UNVERIFIABLE.ordinal()] = 2;
      } catch (NoSuchFieldError var7) {
      }

      try {
         $SwitchMap$com$mojang$realmsclient$dto$RealmsServer$Compatibility[RealmsServer$Compatibility.NEEDS_DOWNGRADE.ordinal()] = 3;
      } catch (NoSuchFieldError var6) {
      }

      try {
         $SwitchMap$com$mojang$realmsclient$dto$RealmsServer$Compatibility[RealmsServer$Compatibility.NEEDS_UPGRADE.ordinal()] = 4;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$com$mojang$realmsclient$dto$RealmsServer$Compatibility[RealmsServer$Compatibility.INCOMPATIBLE.ordinal()] = 5;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$com$mojang$realmsclient$dto$RealmsServer$Compatibility[RealmsServer$Compatibility.RELEASE_TYPE_INCOMPATIBLE.ordinal()] = 6;
      } catch (NoSuchFieldError var3) {
      }

      $SwitchMap$com$mojang$realmsclient$client$RealmsClient$Environment = new int[RealmsClient$Environment.values().length];

      try {
         $SwitchMap$com$mojang$realmsclient$client$RealmsClient$Environment[RealmsClient$Environment.STAGE.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$com$mojang$realmsclient$client$RealmsClient$Environment[RealmsClient$Environment.LOCAL.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
