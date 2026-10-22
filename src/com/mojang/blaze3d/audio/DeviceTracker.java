package com.mojang.blaze3d.audio;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface DeviceTracker {
   DeviceList currentDevices();

   void tick();

   void forceRefresh();
}
