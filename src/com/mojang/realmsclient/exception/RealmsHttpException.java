package com.mojang.realmsclient.exception;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class RealmsHttpException extends RuntimeException {
   public RealmsHttpException(final String s, final Exception e) {
      super(s, e);
   }
}
