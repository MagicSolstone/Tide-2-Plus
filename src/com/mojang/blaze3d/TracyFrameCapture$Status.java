package com.mojang.blaze3d;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
enum TracyFrameCapture$Status {
   WAITING_FOR_CAPTURE,
   WAITING_FOR_COPY,
   WAITING_FOR_UPLOAD;
}
