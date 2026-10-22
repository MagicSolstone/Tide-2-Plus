package com.mojang.blaze3d.platform;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.main.SilentInitException;

@Environment(EnvType.CLIENT)
public class Window$WindowInitFailed extends SilentInitException {
   public Window$WindowInitFailed(final String message) {
      super(message);
   }
}
