package net.minecraft.server.jsonrpc.methods;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.UserBanListEntry;
import org.jspecify.annotations.Nullable;

record BanlistService$UserBan(NameAndId player, @Nullable String reason, String source, Optional<Instant> expires) {
   private static BanlistService$UserBan from(final UserBanListEntry entry) {
      return new BanlistService$UserBan(
         Objects.requireNonNull(entry.getUser()), entry.getReason(), entry.getSource(), Optional.ofNullable(entry.getExpires()).map(Date::toInstant)
      );
   }

   private UserBanListEntry toBanEntry() {
      return new UserBanListEntry(
         new NameAndId(this.player().id(), this.player().name()), null, this.source(), this.expires().map(Date::from).orElse(null), this.reason()
      );
   }
}
