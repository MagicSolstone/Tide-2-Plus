package com.mojang.realmsclient.gui.screens.configuration;

import com.mojang.realmsclient.dto.RealmsServer$WorldType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class RealmsSlotOptionsScreen$1 {
   static {
      try {
         $SwitchMap$com$mojang$realmsclient$dto$RealmsServer$WorldType[RealmsServer$WorldType.ADVENTUREMAP.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$com$mojang$realmsclient$dto$RealmsServer$WorldType[RealmsServer$WorldType.INSPIRATION.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$com$mojang$realmsclient$dto$RealmsServer$WorldType[RealmsServer$WorldType.EXPERIENCE.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
