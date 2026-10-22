package net.minecraft.network.protocol.status;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public record ServerStatus$Favicon(byte[] iconBytes) {
   private static final String PREFIX = "data:image/png;base64,";
   public static final Codec<ServerStatus$Favicon> CODEC = Codec.STRING.comapFlatMap(string -> {
      if (!string.startsWith("data:image/png;base64,")) {
         return DataResult.error(() -> "Unknown format");
      } else {
         try {
            String base64 = string.substring("data:image/png;base64,".length()).replaceAll("\n", "");
            byte[] iconBytes = Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8));
            return DataResult.success(new ServerStatus$Favicon(iconBytes));
         } catch (IllegalArgumentException var3) {
            return DataResult.error(() -> "Malformed base64 server icon");
         }
      }
   }, favicon -> "data:image/png;base64," + new String(Base64.getEncoder().encode(favicon.iconBytes), StandardCharsets.UTF_8));
}
