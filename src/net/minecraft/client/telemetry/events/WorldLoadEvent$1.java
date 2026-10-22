package net.minecraft.client.telemetry.events;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.GameType;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class WorldLoadEvent$1 {
   static {
      try {
         $SwitchMap$net$minecraft$world$level$GameType[GameType.SURVIVAL.ordinal()] = 1;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$GameType[GameType.CREATIVE.ordinal()] = 2;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$GameType[GameType.ADVENTURE.ordinal()] = 3;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$GameType[GameType.SPECTATOR.ordinal()] = 4;
      } catch (NoSuchFieldError var1) {
      }
   }
}
