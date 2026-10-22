package com.mojang.blaze3d.vertex;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface UberGpuBuffer$UploadCallback<T> {
   void bufferHasBeenUploaded(T key);
}
