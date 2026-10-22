package net.minecraft.server.packs;

import com.google.common.hash.HashCode;
import java.net.URL;
import org.jspecify.annotations.Nullable;

public record DownloadQueue$DownloadRequest(URL url, @Nullable HashCode hash) {
}
