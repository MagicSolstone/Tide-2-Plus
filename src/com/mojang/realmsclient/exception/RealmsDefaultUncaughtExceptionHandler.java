package com.mojang.realmsclient.exception;

import java.lang.Thread.UncaughtExceptionHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class RealmsDefaultUncaughtExceptionHandler implements UncaughtExceptionHandler {
   private final Logger logger;

   public RealmsDefaultUncaughtExceptionHandler(final Logger logger) {
      this.logger = logger;
   }

   @Override
   public void uncaughtException(final Thread t, final Throwable e) {
      this.logger.error("Caught previously unhandled exception", e);
   }
}
