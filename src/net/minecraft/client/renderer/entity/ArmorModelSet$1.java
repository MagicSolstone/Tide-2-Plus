package net.minecraft.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.EquipmentSlot;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class ArmorModelSet$1 {
   static {
      try {
         $SwitchMap$net$minecraft$world$entity$EquipmentSlot[EquipmentSlot.HEAD.ordinal()] = 1;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$net$minecraft$world$entity$EquipmentSlot[EquipmentSlot.CHEST.ordinal()] = 2;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$world$entity$EquipmentSlot[EquipmentSlot.LEGS.ordinal()] = 3;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$entity$EquipmentSlot[EquipmentSlot.FEET.ordinal()] = 4;
      } catch (NoSuchFieldError var1) {
      }
   }
}
