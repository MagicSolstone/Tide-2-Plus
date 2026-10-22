package net.minecraft.server.jsonrpc.methods;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record DiscoveryService$DiscoverInfo(String title, String version) {
   public static final MapCodec<DiscoveryService$DiscoverInfo> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Codec.STRING.fieldOf("title").forGetter(DiscoveryService$DiscoverInfo::title),
            Codec.STRING.fieldOf("version").forGetter(DiscoveryService$DiscoverInfo::version)
         )
         .apply(i, DiscoveryService$DiscoverInfo::new)
   );
}
