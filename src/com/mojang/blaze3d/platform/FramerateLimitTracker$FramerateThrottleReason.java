package com.mojang.blaze3d.platform;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum FramerateLimitTracker$FramerateThrottleReason {
   NONE,
   WINDOW_ICONIFIED,
   LONG_AFK,
   SHORT_AFK,
   OUT_OF_LEVEL_MENU;
}
