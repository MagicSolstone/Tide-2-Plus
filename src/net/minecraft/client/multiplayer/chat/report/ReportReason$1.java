package net.minecraft.client.multiplayer.chat.report;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class ReportReason$1 {
   static {
      try {
         $SwitchMap$net$minecraft$client$multiplayer$chat$report$ReportType[ReportType.CHAT.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$client$multiplayer$chat$report$ReportType[ReportType.SKIN.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
