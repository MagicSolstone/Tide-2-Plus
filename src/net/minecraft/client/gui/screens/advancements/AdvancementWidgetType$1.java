package net.minecraft.client.gui.screens.advancements;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancements.AdvancementType;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class AdvancementWidgetType$1 {
   static {
      try {
         $SwitchMap$net$minecraft$advancements$AdvancementType[AdvancementType.TASK.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$advancements$AdvancementType[AdvancementType.CHALLENGE.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$advancements$AdvancementType[AdvancementType.GOAL.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
