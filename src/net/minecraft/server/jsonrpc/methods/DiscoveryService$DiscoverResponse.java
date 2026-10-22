package net.minecraft.server.jsonrpc.methods;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.server.jsonrpc.api.MethodInfo$Named;

public record DiscoveryService$DiscoverResponse(
   String jsonRpcProtocolVersion,
   DiscoveryService$DiscoverInfo discoverInfo,
   List<MethodInfo$Named<?, ?>> methods,
   DiscoveryService$DiscoverComponents components
) {
   public static final MapCodec<DiscoveryService$DiscoverResponse> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Codec.STRING.fieldOf("openrpc").forGetter(DiscoveryService$DiscoverResponse::jsonRpcProtocolVersion),
            DiscoveryService$DiscoverInfo.CODEC.codec().fieldOf("info").forGetter(DiscoveryService$DiscoverResponse::discoverInfo),
            Codec.list(MethodInfo$Named.CODEC).fieldOf("methods").forGetter(DiscoveryService$DiscoverResponse::methods),
            DiscoveryService$DiscoverComponents.CODEC.codec().fieldOf("components").forGetter(DiscoveryService$DiscoverResponse::components)
         )
         .apply(i, DiscoveryService$DiscoverResponse::new)
   );
}
