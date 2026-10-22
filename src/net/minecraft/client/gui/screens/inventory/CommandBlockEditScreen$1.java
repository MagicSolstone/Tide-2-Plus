package net.minecraft.client.gui.screens.inventory;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.entity.CommandBlockEntity$Mode;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class CommandBlockEditScreen$1 {
   static {
      try {
         $SwitchMap$net$minecraft$world$level$block$entity$CommandBlockEntity$Mode[CommandBlockEntity$Mode.SEQUENCE.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$entity$CommandBlockEntity$Mode[CommandBlockEntity$Mode.AUTO.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$entity$CommandBlockEntity$Mode[CommandBlockEntity$Mode.REDSTONE.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
