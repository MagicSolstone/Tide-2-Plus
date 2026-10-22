package net.minecraft.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.state.properties.ChestType;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class MultiblockChestResources$1 {
   static {
      try {
         $SwitchMap$net$minecraft$world$level$block$state$properties$ChestType[ChestType.SINGLE.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$state$properties$ChestType[ChestType.LEFT.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$state$properties$ChestType[ChestType.RIGHT.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
