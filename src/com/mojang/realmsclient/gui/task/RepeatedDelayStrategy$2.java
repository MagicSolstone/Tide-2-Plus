package com.mojang.realmsclient.gui.task;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
class RepeatedDelayStrategy$2 implements RepeatedDelayStrategy {
   private static final Logger LOGGER = LogUtils.getLogger();
   private int failureCount;

   RepeatedDelayStrategy$2(final int val$maxBackoff) {
      this.val$maxBackoff = val$maxBackoffx;
   }

   @Override
   public long delayCyclesAfterSuccess() {
      this.failureCount = 0;
      return 1L;
   }

   @Override
   public long delayCyclesAfterFailure() {
      this.failureCount++;
      long expandedDelay = Math.min(1L << this.failureCount, (long)this.val$maxBackoff);
      LOGGER.debug("Skipping for {} extra cycles", expandedDelay);
      return expandedDelay;
   }
}
