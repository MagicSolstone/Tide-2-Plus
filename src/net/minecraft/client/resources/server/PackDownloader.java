package net.minecraft.client.resources.server;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.packs.DownloadQueue$BatchResult;
import net.minecraft.server.packs.DownloadQueue$DownloadRequest;

@Environment(EnvType.CLIENT)
public interface PackDownloader {
   void download(Map<UUID, DownloadQueue$DownloadRequest> requests, Consumer<DownloadQueue$BatchResult> output);
}
