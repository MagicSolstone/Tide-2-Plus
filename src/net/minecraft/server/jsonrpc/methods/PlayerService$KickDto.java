package net.minecraft.server.jsonrpc.methods;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.server.jsonrpc.api.PlayerDto;

public record PlayerService$KickDto(PlayerDto player, Optional<Message> message) {
   public static final MapCodec<PlayerService$KickDto> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            PlayerDto.CODEC.codec().fieldOf("player").forGetter(PlayerService$KickDto::player),
            Message.CODEC.optionalFieldOf("message").forGetter(PlayerService$KickDto::message)
         )
         .apply(i, PlayerService$KickDto::new)
   );
}
