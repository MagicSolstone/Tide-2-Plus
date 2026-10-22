package net.minecraft.world.level.levelgen.densityfunction.generator;

import net.minecraft.core.Direction$Axis;
import net.minecraft.world.level.levelgen.densityfunction.TilingMode;

// $VF: synthetic class
class GradientFunction$1 {
   static {
      try {
         $SwitchMap$net$minecraft$core$Direction$Axis[Direction$Axis.X.ordinal()] = 1;
      } catch (NoSuchFieldError var6) {
      }

      try {
         $SwitchMap$net$minecraft$core$Direction$Axis[Direction$Axis.Y.ordinal()] = 2;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$net$minecraft$core$Direction$Axis[Direction$Axis.Z.ordinal()] = 3;
      } catch (NoSuchFieldError var4) {
      }

      $SwitchMap$net$minecraft$world$level$levelgen$densityfunction$TilingMode = new int[TilingMode.values().length];

      try {
         $SwitchMap$net$minecraft$world$level$levelgen$densityfunction$TilingMode[TilingMode.CLAMP_TO_EDGE.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$levelgen$densityfunction$TilingMode[TilingMode.REPEAT.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$levelgen$densityfunction$TilingMode[TilingMode.MIRRORED_REPEAT.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
