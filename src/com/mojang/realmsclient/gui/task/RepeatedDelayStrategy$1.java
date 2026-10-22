package com.mojang.realmsclient.gui.task;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class RepeatedDelayStrategy$1 implements RepeatedDelayStrategy {
   @Override
   public long delayCyclesAfterSuccess() {
      return 1L;
   }

   @Override
   public long delayCyclesAfterFailure() {
      return 1L;
   }
}
