package net.minecraft.server.players;

import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.services.ProfileNotFoundException;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;

class OldUsersConverter$2 implements ProfileLookupCallback {
   OldUsersConverter$2(final MinecraftServer val$server, final ServerOpList val$opsList) {
      this.val$server = val$serverx;
      this.val$opsList = val$opsListx;
   }

   public void onProfileLookupSucceeded(final String profileName, final UUID profileId) {
      NameAndId profile = new NameAndId(profileId, profileName);
      this.val$server.services().nameToIdCache().add(profile);
      this.val$opsList.add(new ServerOpListEntry(profile, this.val$server.operatorUserPermissions(), false));
   }

   public void onProfileLookupFailed(final String profileName, final Exception exception) {
      OldUsersConverter.LOGGER.warn("Could not lookup oplist entry for {}", profileName, exception);
      if (!(exception instanceof ProfileNotFoundException)) {
         throw new OldUsersConverter$ConversionError("Could not request user " + profileName + " from backend systems", exception);
      }
   }
}
