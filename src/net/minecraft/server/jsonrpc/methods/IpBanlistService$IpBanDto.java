package net.minecraft.server.jsonrpc.methods;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.time.Instant;
import java.util.Optional;
import net.minecraft.server.players.IpBanListEntry;
import net.minecraft.util.ExtraCodecs;

public record IpBanlistService$IpBanDto(String ip, Optional<String> reason, Optional<String> source, Optional<Instant> expires) {
   public static final MapCodec<IpBanlistService$IpBanDto> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Codec.STRING.fieldOf("ip").forGetter(IpBanlistService$IpBanDto::ip),
            Codec.STRING.optionalFieldOf("reason").forGetter(IpBanlistService$IpBanDto::reason),
            Codec.STRING.optionalFieldOf("source").forGetter(IpBanlistService$IpBanDto::source),
            ExtraCodecs.INSTANT_ISO8601.optionalFieldOf("expires").forGetter(IpBanlistService$IpBanDto::expires)
         )
         .apply(i, IpBanlistService$IpBanDto::new)
   );

   private static IpBanlistService$IpBanDto from(final IpBanlistService$IpBan ban) {
      return new IpBanlistService$IpBanDto(ban.ip(), Optional.ofNullable(ban.reason()), Optional.of(ban.source()), ban.expires());
   }

   public static IpBanlistService$IpBanDto from(final IpBanListEntry ban) {
      return from(IpBanlistService$IpBan.from(ban));
   }

   private IpBanlistService$IpBan toIpBan() {
      return new IpBanlistService$IpBan(this.ip(), this.reason().orElse(null), this.source().orElse("Management server"), this.expires());
   }
}
