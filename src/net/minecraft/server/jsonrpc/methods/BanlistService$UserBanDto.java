package net.minecraft.server.jsonrpc.methods;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.time.Instant;
import java.util.Optional;
import net.minecraft.server.jsonrpc.api.PlayerDto;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.util.ExtraCodecs;

public record BanlistService$UserBanDto(PlayerDto player, Optional<String> reason, Optional<String> source, Optional<Instant> expires) {
   public static final MapCodec<BanlistService$UserBanDto> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            PlayerDto.CODEC.codec().fieldOf("player").forGetter(BanlistService$UserBanDto::player),
            Codec.STRING.optionalFieldOf("reason").forGetter(BanlistService$UserBanDto::reason),
            Codec.STRING.optionalFieldOf("source").forGetter(BanlistService$UserBanDto::source),
            ExtraCodecs.INSTANT_ISO8601.optionalFieldOf("expires").forGetter(BanlistService$UserBanDto::expires)
         )
         .apply(i, BanlistService$UserBanDto::new)
   );

   private static BanlistService$UserBanDto from(final BanlistService$UserBan ban) {
      return new BanlistService$UserBanDto(PlayerDto.from(ban.player()), Optional.ofNullable(ban.reason()), Optional.of(ban.source()), ban.expires());
   }

   public static BanlistService$UserBanDto from(final UserBanListEntry entry) {
      return from(BanlistService$UserBan.from(entry));
   }

   private BanlistService$UserBan toUserBan(final NameAndId nameAndId) {
      return new BanlistService$UserBan(nameAndId, this.reason().orElse(null), this.source().orElse("Management server"), this.expires());
   }
}
