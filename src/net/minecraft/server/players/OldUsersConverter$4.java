package net.minecraft.server.players;

import com.mojang.authlib.ProfileLookupCallback;
import java.util.List;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;

class OldUsersConverter$4 implements ProfileLookupCallback {
   OldUsersConverter$4(final MinecraftServer val$server, final List val$profiles) {
      this.val$server = val$serverx;
      this.val$profiles = val$profilesx;
   }

   public void onProfileLookupSucceeded(final String profileName, final UUID profileId) {
      NameAndId profile = new NameAndId(profileId, profileName);
      this.val$server.services().nameToIdCache().add(profile);
      this.val$profiles.add(profile);
   }

   public void onProfileLookupFailed(final String profileName, final Exception exception) {
      OldUsersConverter.LOGGER.warn("Could not lookup user whitelist entry for {}", profileName, exception);
   }
}
