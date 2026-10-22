package net.minecraft.server.jsonrpc.methods;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.server.jsonrpc.api.PlayerDto;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.server.players.ServerOpListEntry;

public record OperatorService$OperatorDto(PlayerDto player, Optional<PermissionLevel> permissionLevel, Optional<Boolean> bypassesPlayerLimit) {
   public static final MapCodec<OperatorService$OperatorDto> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            PlayerDto.CODEC.codec().fieldOf("player").forGetter(OperatorService$OperatorDto::player),
            PermissionLevel.INT_CODEC.optionalFieldOf("permissionLevel").forGetter(OperatorService$OperatorDto::permissionLevel),
            Codec.BOOL.optionalFieldOf("bypassesPlayerLimit").forGetter(OperatorService$OperatorDto::bypassesPlayerLimit)
         )
         .apply(i, OperatorService$OperatorDto::new)
   );

   public static OperatorService$OperatorDto from(final ServerOpListEntry serverOpListEntry) {
      return new OperatorService$OperatorDto(
         PlayerDto.from(Objects.requireNonNull(serverOpListEntry.getUser())),
         Optional.of(serverOpListEntry.permissions().level()),
         Optional.of(serverOpListEntry.getBypassesPlayerLimit())
      );
   }
}
