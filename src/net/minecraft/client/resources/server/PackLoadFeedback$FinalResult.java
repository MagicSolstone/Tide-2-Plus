package net.minecraft.client.resources.server;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum PackLoadFeedback$FinalResult {
   DECLINED,
   APPLIED,
   DISCARDED,
   DOWNLOAD_FAILED,
   ACTIVATION_FAILED;
}
