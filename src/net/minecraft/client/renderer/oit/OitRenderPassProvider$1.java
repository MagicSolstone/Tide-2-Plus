package net.minecraft.client.renderer.oit;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class OitRenderPassProvider$1 {
   static {
      try {
         $SwitchMap$net$minecraft$client$renderer$oit$OitStage[OitStage.DEPTH_BOUNDS.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$client$renderer$oit$OitStage[OitStage.TRANSMITTANCE.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$client$renderer$oit$OitStage[OitStage.ACCUMULATE.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
