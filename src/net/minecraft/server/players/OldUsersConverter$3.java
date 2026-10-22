package net.minecraft.server.players;

import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.services.ProfileNotFoundException;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;

class OldUsersConverter$3 implements ProfileLookupCallback {
   OldUsersConverter$3(final MinecraftServer val$server, final UserWhiteList val$whitelist) {
      this.val$server = val$serverx;
      this.val$whitelist = val$whitelistx;
   }

   public void onProfileLookupSucceeded(final String profileName, final UUID profileId) {
      NameAndId profile = new NameAndId(profileId, profileName);
      this.val$server.services().nameToIdCache().add(profile);
      this.val$whitelist.add(new UserWhiteListEntry(profile));
   }

   public void onProfileLookupFailed(final String profileName, final Exception exception) {
      OldUsersConverter.LOGGER.warn("Could not lookup user whitelist entry for {}", profileName, exception);
      if (!(exception instanceof ProfileNotFoundException)) {
         throw new OldUsersConverter$ConversionError("Could not request user " + profileName + " from backend systems", exception);
      }
   }
}
