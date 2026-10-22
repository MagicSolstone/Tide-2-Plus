package net.minecraft.server.jsonrpc.methods;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.minecraft.server.jsonrpc.api.PlayerDto;

public record ServerStateService$SystemMessage(Message message, boolean overlay, Optional<List<PlayerDto>> receivingPlayers) {
   public static final Codec<ServerStateService$SystemMessage> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Message.CODEC.fieldOf("message").forGetter(ServerStateService$SystemMessage::message),
            Codec.BOOL.fieldOf("overlay").forGetter(ServerStateService$SystemMessage::overlay),
            PlayerDto.CODEC.codec().listOf().lenientOptionalFieldOf("receivingPlayers").forGetter(ServerStateService$SystemMessage::receivingPlayers)
         )
         .apply(i, ServerStateService$SystemMessage::new)
   );
}
