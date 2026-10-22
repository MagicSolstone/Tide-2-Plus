package net.minecraft.server.packs;

import com.google.common.hash.HashFunction;
import java.net.Proxy;
import java.util.Map;
import net.minecraft.util.HttpUtil$DownloadProgressListener;

public record DownloadQueue$BatchConfig(
   HashFunction hashFunction, int maxSize, Map<String, String> headers, Proxy proxy, HttpUtil$DownloadProgressListener listener
) {
}
