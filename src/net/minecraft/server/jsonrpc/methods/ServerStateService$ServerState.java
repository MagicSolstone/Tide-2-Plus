package net.minecraft.server.jsonrpc.methods;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.network.protocol.status.ServerStatus$Version;
import net.minecraft.server.jsonrpc.api.PlayerDto;

public record ServerStateService$ServerState(boolean started, List<PlayerDto> players, ServerStatus$Version version) {
   public static final Codec<ServerStateService$ServerState> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.BOOL.fieldOf("started").forGetter(ServerStateService$ServerState::started),
            PlayerDto.CODEC.codec().listOf().lenientOptionalFieldOf("players", List.of()).forGetter(ServerStateService$ServerState::players),
            ServerStatus$Version.CODEC.fieldOf("version").forGetter(ServerStateService$ServerState::version)
         )
         .apply(i, ServerStateService$ServerState::new)
   );
   public static final ServerStateService$ServerState NOT_STARTED = new ServerStateService$ServerState(false, List.of(), ServerStatus$Version.current());
}
