package com.mojang.blaze3d;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.event.Level;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class TracyBootstrap$1 {
   static {
      try {
         $SwitchMap$org$slf4j$event$Level[Level.DEBUG.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$org$slf4j$event$Level[Level.WARN.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$org$slf4j$event$Level[Level.ERROR.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
