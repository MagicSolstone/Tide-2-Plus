package net.minecraft.server.jsonrpc.methods;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.server.players.IpBanListEntry;
import org.jspecify.annotations.Nullable;

record IpBanlistService$IpBan(String ip, @Nullable String reason, String source, Optional<Instant> expires) {
   private static IpBanlistService$IpBan from(final IpBanListEntry entry) {
      return new IpBanlistService$IpBan(
         Objects.requireNonNull(entry.getUser()), entry.getReason(), entry.getSource(), Optional.ofNullable(entry.getExpires()).map(Date::toInstant)
      );
   }

   private IpBanListEntry toIpBanEntry() {
      return new IpBanListEntry(this.ip(), null, this.source(), this.expires().map(Date::from).orElse(null), this.reason());
   }
}
