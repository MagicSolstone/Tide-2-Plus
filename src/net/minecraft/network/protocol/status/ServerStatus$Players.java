package net.minecraft.network.protocol.status;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.server.players.NameAndId;

public record ServerStatus$Players(int max, int online, List<NameAndId> sample) {
   public static final Codec<ServerStatus$Players> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.INT.fieldOf("max").forGetter(ServerStatus$Players::max),
            Codec.INT.fieldOf("online").forGetter(ServerStatus$Players::online),
            NameAndId.CODEC.listOf().lenientOptionalFieldOf("sample", List.of()).forGetter(ServerStatus$Players::sample)
         )
         .apply(i, ServerStatus$Players::new)
   );
}
