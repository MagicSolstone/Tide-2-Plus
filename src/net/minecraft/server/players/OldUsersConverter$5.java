package net.minecraft.server.players;

import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.services.ProfileNotFoundException;
import java.io.File;
import java.util.UUID;
import net.minecraft.server.dedicated.DedicatedServer;

class OldUsersConverter$5 implements ProfileLookupCallback {
   OldUsersConverter$5(
      final DedicatedServer val$server,
      final File val$worldNewPlayerDirectory,
      final File val$unknownPlayerDirectory,
      final File val$worldPlayerDirectory,
      final String[] val$names
   ) {
      this.val$server = val$serverx;
      this.val$worldNewPlayerDirectory = val$worldNewPlayerDirectoryx;
      this.val$unknownPlayerDirectory = val$unknownPlayerDirectoryx;
      this.val$worldPlayerDirectory = val$worldPlayerDirectoryx;
      this.val$names = val$namesx;
   }

   public void onProfileLookupSucceeded(final String profileName, final UUID profileId) {
      NameAndId profile = new NameAndId(profileId, profileName);
      this.val$server.services().nameToIdCache().add(profile);
      this.movePlayerFile(this.val$worldNewPlayerDirectory, this.getFileNameForProfile(profileName), profileId.toString());
   }

   public void onProfileLookupFailed(final String profileName, final Exception exception) {
      OldUsersConverter.LOGGER.warn("Could not lookup user uuid for {}", profileName, exception);
      if (exception instanceof ProfileNotFoundException) {
         String fileNameForProfile = this.getFileNameForProfile(profileName);
         this.movePlayerFile(this.val$unknownPlayerDirectory, fileNameForProfile, fileNameForProfile);
      } else {
         throw new OldUsersConverter$ConversionError("Could not request user " + profileName + " from backend systems", exception);
      }
   }

   private void movePlayerFile(final File directory, final String oldName, final String newName) {
      File oldFileName = new File(this.val$worldPlayerDirectory, oldName + ".dat");
      File newFileName = new File(directory, newName + ".dat");
      OldUsersConverter.ensureDirectoryExists(directory);
      if (!oldFileName.renameTo(newFileName)) {
         throw new OldUsersConverter$ConversionError("Could not convert file for " + oldName);
      }
   }

   private String getFileNameForProfile(final String profileName) {
      String fileName = null;

      for (String name : this.val$names) {
         if (name != null && name.equalsIgnoreCase(profileName)) {
            fileName = name;
            break;
         }
      }

      if (fileName == null) {
         throw new OldUsersConverter$ConversionError("Could not find the filename for " + profileName + " anymore");
      } else {
         return fileName;
      }
   }
}
