package net.minecraft.server.players;

import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.services.ProfileNotFoundException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;

class OldUsersConverter$1 implements ProfileLookupCallback {
   OldUsersConverter$1(final MinecraftServer val$server, final Map val$userMap, final UserBanList val$bans) {
      this.val$server = val$serverx;
      this.val$userMap = val$userMapx;
      this.val$bans = val$bansx;
   }

   public void onProfileLookupSucceeded(final String profileName, final UUID profileId) {
      NameAndId profile = new NameAndId(profileId, profileName);
      this.val$server.services().nameToIdCache().add(profile);
      String[] userDef = (String[])this.val$userMap.get(profile.name().toLowerCase(Locale.ROOT));
      if (userDef == null) {
         OldUsersConverter.LOGGER.warn("Could not convert user banlist entry for {}", profile.name());
         throw new OldUsersConverter$ConversionError("Profile not in the conversionlist");
      } else {
         Date created = userDef.length > 1 ? OldUsersConverter.parseDate(userDef[1], null) : null;
         String source = userDef.length > 2 ? userDef[2] : null;
         Date expires = userDef.length > 3 ? OldUsersConverter.parseDate(userDef[3], null) : null;
         String reason = userDef.length > 4 ? userDef[4] : null;
         this.val$bans.add(new UserBanListEntry(profile, created, source, expires, reason));
      }
   }

   public void onProfileLookupFailed(final String profileName, final Exception exception) {
      OldUsersConverter.LOGGER.warn("Could not lookup user banlist entry for {}", profileName, exception);
      if (!(exception instanceof ProfileNotFoundException)) {
         throw new OldUsersConverter$ConversionError("Could not request user " + profileName + " from backend systems", exception);
      }
   }
}
