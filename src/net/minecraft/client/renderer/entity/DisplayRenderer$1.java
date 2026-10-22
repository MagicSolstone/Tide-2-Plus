package net.minecraft.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.Display$BillboardConstraints;
import net.minecraft.world.entity.Display$TextDisplay$Align;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class DisplayRenderer$1 {
   static {
      try {
         $SwitchMap$net$minecraft$world$entity$Display$TextDisplay$Align[Display$TextDisplay$Align.LEFT.ordinal()] = 1;
      } catch (NoSuchFieldError var7) {
      }

      try {
         $SwitchMap$net$minecraft$world$entity$Display$TextDisplay$Align[Display$TextDisplay$Align.RIGHT.ordinal()] = 2;
      } catch (NoSuchFieldError var6) {
      }

      try {
         $SwitchMap$net$minecraft$world$entity$Display$TextDisplay$Align[Display$TextDisplay$Align.CENTER.ordinal()] = 3;
      } catch (NoSuchFieldError var5) {
      }

      $SwitchMap$net$minecraft$world$entity$Display$BillboardConstraints = new int[Display$BillboardConstraints.values().length];

      try {
         $SwitchMap$net$minecraft$world$entity$Display$BillboardConstraints[Display$BillboardConstraints.FIXED.ordinal()] = 1;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$net$minecraft$world$entity$Display$BillboardConstraints[Display$BillboardConstraints.HORIZONTAL.ordinal()] = 2;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$world$entity$Display$BillboardConstraints[Display$BillboardConstraints.VERTICAL.ordinal()] = 3;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$entity$Display$BillboardConstraints[Display$BillboardConstraints.CENTER.ordinal()] = 4;
      } catch (NoSuchFieldError var1) {
      }
   }
}
