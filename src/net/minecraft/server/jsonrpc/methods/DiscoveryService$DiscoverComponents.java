package net.minecraft.server.jsonrpc.methods;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import net.minecraft.server.jsonrpc.api.Schema;

public record DiscoveryService$DiscoverComponents(Map<String, Schema<?>> schemas) {
   public static final MapCodec<DiscoveryService$DiscoverComponents> CODEC = typedSchema();

   private static MapCodec<DiscoveryService$DiscoverComponents> typedSchema() {
      return RecordCodecBuilder.mapCodec(
         i -> i.group(Codec.unboundedMap(Codec.STRING, Schema.CODEC).fieldOf("schemas").forGetter(DiscoveryService$DiscoverComponents::schemas))
            .apply(i, DiscoveryService$DiscoverComponents::new)
      );
   }
}
