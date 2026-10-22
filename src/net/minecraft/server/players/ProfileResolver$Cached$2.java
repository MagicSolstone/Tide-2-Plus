package net.minecraft.server.players;

import com.google.common.cache.CacheLoader;
import com.mojang.authlib.GameProfile;
import java.util.Objects;
import java.util.Optional;

class ProfileResolver$Cached$2 extends CacheLoader<String, Optional<GameProfile>> {
   ProfileResolver$Cached$2(final ProfileResolver$Cached this$0, final UserNameToIdResolver val$nameToIdCache) {
      this.val$nameToIdCache = val$nameToIdCachex;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   public Optional<GameProfile> load(final String name) {
      return this.val$nameToIdCache.get(name).flatMap(nameAndId -> (Optional<? extends GameProfile>)this.this$0.profileCacheById.getUnchecked(nameAndId.id()));
   }
}
