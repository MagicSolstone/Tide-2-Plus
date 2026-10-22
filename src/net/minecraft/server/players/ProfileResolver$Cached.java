package net.minecraft.server.players;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.SessionService;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.util.StringUtil;

public class ProfileResolver$Cached implements ProfileResolver {
   private final LoadingCache<String, Optional<GameProfile>> profileCacheByName;
   private final LoadingCache<UUID, Optional<GameProfile>> profileCacheById;

   public ProfileResolver$Cached(final SessionService sessionService, final UserNameToIdResolver nameToIdCache) {
      this.profileCacheById = CacheBuilder.newBuilder()
         .expireAfterAccess(Duration.ofMinutes(10L))
         .maximumSize(256L)
         .build(new ProfileResolver$Cached$1(this, sessionService));
      this.profileCacheByName = CacheBuilder.newBuilder()
         .expireAfterAccess(Duration.ofMinutes(10L))
         .maximumSize(256L)
         .build(new ProfileResolver$Cached$2(this, nameToIdCache));
   }

   @Override
   public Optional<GameProfile> fetchByName(final String name) {
      return StringUtil.isValidPlayerName(name) ? (Optional)this.profileCacheByName.getUnchecked(name) : Optional.empty();
   }

   @Override
   public Optional<GameProfile> fetchById(final UUID id) {
      return (Optional<GameProfile>)this.profileCacheById.getUnchecked(id);
   }
}
