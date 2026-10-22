package net.minecraft.server.players;

import com.google.common.cache.CacheLoader;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.SessionService;
import com.mojang.authlib.services.ProfileResult;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

class ProfileResolver$Cached$1 extends CacheLoader<UUID, Optional<GameProfile>> {
   ProfileResolver$Cached$1(final ProfileResolver$Cached this$0, final SessionService val$sessionService) {
      this.val$sessionService = val$sessionServicex;
      Objects.requireNonNull(this$0);
      super();
   }

   public Optional<GameProfile> load(final UUID profileId) {
      ProfileResult result = this.val$sessionService.fetchProfile(profileId, true);
      return Optional.ofNullable(result).map(ProfileResult::profile);
   }
}
