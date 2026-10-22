package com.mojang.realmsclient.gui.task;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface RepeatedDelayStrategy {
   RepeatedDelayStrategy CONSTANT = new RepeatedDelayStrategy$1();

   long delayCyclesAfterSuccess();

   long delayCyclesAfterFailure();

   static RepeatedDelayStrategy exponentialBackoff(final int maxBackoff) {
      return new RepeatedDelayStrategy$2(maxBackoff);
   }
}
