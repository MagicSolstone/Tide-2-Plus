package com.mojang.realmsclient.gui.task;

import com.mojang.logging.LogUtils;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.TimeSource;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class DataFetcher {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final Executor executor;
   private final TimeUnit resolution;
   private final TimeSource timeSource;

   public DataFetcher(final Executor executor, final TimeUnit resolution, final TimeSource timeSource) {
      this.executor = executor;
      this.resolution = resolution;
      this.timeSource = timeSource;
   }

   public <T> DataFetcher$Task<T> createTask(final String id, final Callable<T> updater, final Duration period, final RepeatedDelayStrategy repeatStrategy) {
      long periodInUnit = this.resolution.convert(period);
      if (periodInUnit == 0L) {
         throw new IllegalArgumentException("Period of " + period + " too short for selected resolution of " + this.resolution);
      } else {
         return new DataFetcher$Task<>(this, id, (long)updater, periodInUnit, repeatStrategy);
      }
   }

   public DataFetcher$Subscription createSubscription() {
      return new DataFetcher$Subscription(this);
   }
}
