package net.minecraft.client.gui.screens.inventory;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.properties.StructureMode;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class StructureBlockEditScreen$2 {
   static {
      try {
         $SwitchMap$net$minecraft$world$level$block$state$properties$StructureMode[StructureMode.SAVE.ordinal()] = 1;
      } catch (NoSuchFieldError var8) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$state$properties$StructureMode[StructureMode.LOAD.ordinal()] = 2;
      } catch (NoSuchFieldError var7) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$state$properties$StructureMode[StructureMode.CORNER.ordinal()] = 3;
      } catch (NoSuchFieldError var6) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$state$properties$StructureMode[StructureMode.DATA.ordinal()] = 4;
      } catch (NoSuchFieldError var5) {
      }

      $SwitchMap$net$minecraft$world$level$block$Rotation = new int[Rotation.values().length];

      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.NONE.ordinal()] = 1;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.CLOCKWISE_180.ordinal()] = 2;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.COUNTERCLOCKWISE_90.ordinal()] = 3;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.CLOCKWISE_90.ordinal()] = 4;
      } catch (NoSuchFieldError var1) {
      }
   }
}
