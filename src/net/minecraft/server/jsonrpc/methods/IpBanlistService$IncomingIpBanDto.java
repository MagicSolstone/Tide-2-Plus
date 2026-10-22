package net.minecraft.server.jsonrpc.methods;

import com.google.common.net.InetAddresses;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.time.Instant;
import java.util.Optional;
import net.minecraft.server.jsonrpc.api.PlayerDto;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import org.jspecify.annotations.Nullable;

public record IpBanlistService$IncomingIpBanDto(
   Optional<PlayerDto> player, Optional<String> ip, Optional<String> reason, Optional<String> source, Optional<Instant> expires
) {
   public static final MapCodec<IpBanlistService$IncomingIpBanDto> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            PlayerDto.CODEC.codec().optionalFieldOf("player").forGetter(IpBanlistService$IncomingIpBanDto::player),
            Codec.STRING.optionalFieldOf("ip").forGetter(IpBanlistService$IncomingIpBanDto::ip),
            Codec.STRING.optionalFieldOf("reason").forGetter(IpBanlistService$IncomingIpBanDto::reason),
            Codec.STRING.optionalFieldOf("source").forGetter(IpBanlistService$IncomingIpBanDto::source),
            ExtraCodecs.INSTANT_ISO8601.optionalFieldOf("expires").forGetter(IpBanlistService$IncomingIpBanDto::expires)
         )
         .apply(i, IpBanlistService$IncomingIpBanDto::new)
   );

   private IpBanlistService$IpBan toIpBan(final ServerPlayer player) {
      return new IpBanlistService$IpBan(player.getIpAddress(), this.reason().orElse(null), this.source().orElse("Management server"), this.expires());
   }

   @Nullable
   private IpBanlistService$IpBan toIpBan() {
      return !this.ip().isEmpty() && InetAddresses.isInetAddress(this.ip().get())
         ? new IpBanlistService$IpBan(this.ip().get(), this.reason().orElse(null), this.source().orElse("Management server"), this.expires())
         : null;
   }
}
