package net.minecraft.client.renderer.blockentity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.WeatheringCopper$WeatherState;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class ChestRenderer$1 {
   static {
      try {
         $SwitchMap$net$minecraft$world$level$block$WeatheringCopper$WeatherState[WeatheringCopper$WeatherState.UNAFFECTED.ordinal()] = 1;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$WeatheringCopper$WeatherState[WeatheringCopper$WeatherState.EXPOSED.ordinal()] = 2;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$WeatheringCopper$WeatherState[WeatheringCopper$WeatherState.WEATHERED.ordinal()] = 3;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$WeatheringCopper$WeatherState[WeatheringCopper$WeatherState.OXIDIZED.ordinal()] = 4;
      } catch (NoSuchFieldError var1) {
      }
   }
}
