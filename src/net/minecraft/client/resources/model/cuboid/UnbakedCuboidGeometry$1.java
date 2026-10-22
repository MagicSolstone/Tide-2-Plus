package net.minecraft.client.resources.model.cuboid;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction$Axis;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class UnbakedCuboidGeometry$1 {
   static {
      try {
         $SwitchMap$net$minecraft$core$Direction$Axis[Direction$Axis.X.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$core$Direction$Axis[Direction$Axis.Y.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$core$Direction$Axis[Direction$Axis.Z.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
