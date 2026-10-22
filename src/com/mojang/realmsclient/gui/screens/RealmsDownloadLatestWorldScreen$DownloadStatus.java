package com.mojang.realmsclient.gui.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class RealmsDownloadLatestWorldScreen$DownloadStatus {
   public volatile long bytesWritten;
   public volatile long totalBytes;
}
