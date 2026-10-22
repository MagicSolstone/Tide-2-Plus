package net.minecraft.server.players;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.logging.LogUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.notifications.EmptyNotificationService;
import net.minecraft.util.StringUtil;
import net.minecraft.world.level.storage.LevelResource;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class OldUsersConverter {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final File OLD_IPBANLIST = new File("banned-ips.txt");
   public static final File OLD_USERBANLIST = new File("banned-players.txt");
   public static final File OLD_OPLIST = new File("ops.txt");
   public static final File OLD_WHITELIST = new File("white-list.txt");

   private static List<String> readOldListFormat(final File file, final Map<String, String[]> userMap) throws IOException {
      List<String> lines = Files.readLines(file, StandardCharsets.UTF_8);

      for (String line : lines) {
         line = line.trim();
         if (!line.startsWith("#") && !line.isEmpty()) {
            String[] parts = line.split("\\|");
            userMap.put(parts[0].toLowerCase(Locale.ROOT), parts);
         }
      }

      return lines;
   }

   private static void lookupPlayers(final MinecraftServer server, final Collection<String> names, final ProfileLookupCallback callback) {
      String[] filteredNames = names.stream().filter(s -> !StringUtil.isNullOrEmpty(s)).toArray(String[]::new);
      if (server.usesAuthentication()) {
         server.services().profileRepository().findProfilesByNames(filteredNames, callback);
      } else {
         for (String name : filteredNames) {
            callback.onProfileLookupSucceeded(name, UUIDUtil.createOfflinePlayerUUID(name));
         }
      }
   }

   public static boolean convertUserBanlist(final MinecraftServer server) {
      UserBanList bans = new UserBanList(PlayerList.USERBANLIST_FILE, new EmptyNotificationService());
      if (OLD_USERBANLIST.exists() && OLD_USERBANLIST.isFile()) {
         if (bans.getFile().exists()) {
            try {
               bans.load();
            } catch (IOException var6) {
               LOGGER.warn("Could not load existing file {}", bans.getFile().getName(), var6);
            }
         }

         try {
            Map<String, String[]> userMap = Maps.newHashMap();
            readOldListFormat(OLD_USERBANLIST, userMap);
            ProfileLookupCallback callback = new OldUsersConverter$1(server, userMap, bans);
            lookupPlayers(server, userMap.keySet(), callback);
            bans.save();
            renameOldFile(OLD_USERBANLIST);
            return true;
         } catch (IOException var4) {
            LOGGER.warn("Could not read old user banlist to convert it!", var4);
            return false;
         } catch (OldUsersConverter$ConversionError var5) {
            LOGGER.error("Conversion failed, please try again later", var5);
            return false;
         }
      } else {
         return true;
      }
   }

   public static boolean convertIpBanlist(final MinecraftServer server) {
      IpBanList ipBans = new IpBanList(PlayerList.IPBANLIST_FILE, new EmptyNotificationService());
      if (OLD_IPBANLIST.exists() && OLD_IPBANLIST.isFile()) {
         if (ipBans.getFile().exists()) {
            try {
               ipBans.load();
            } catch (IOException var11) {
               LOGGER.warn("Could not load existing file {}", ipBans.getFile().getName(), var11);
            }
         }

         try {
            Map<String, String[]> userMap = Maps.newHashMap();
            readOldListFormat(OLD_IPBANLIST, userMap);

            for (String key : userMap.keySet()) {
               String[] userDef = userMap.get(key);
               Date created = userDef.length > 1 ? parseDate(userDef[1], null) : null;
               String source = userDef.length > 2 ? userDef[2] : null;
               Date expires = userDef.length > 3 ? parseDate(userDef[3], null) : null;
               String reason = userDef.length > 4 ? userDef[4] : null;
               ipBans.add(new IpBanListEntry(key, created, source, expires, reason));
            }

            ipBans.save();
            renameOldFile(OLD_IPBANLIST);
            return true;
         } catch (IOException var10) {
            LOGGER.warn("Could not parse old ip banlist to convert it!", var10);
            return false;
         }
      } else {
         return true;
      }
   }

   public static boolean convertOpsList(final MinecraftServer server) {
      ServerOpList opsList = new ServerOpList(PlayerList.OPLIST_FILE, new EmptyNotificationService());
      if (OLD_OPLIST.exists() && OLD_OPLIST.isFile()) {
         if (opsList.getFile().exists()) {
            try {
               opsList.load();
            } catch (IOException var6) {
               LOGGER.warn("Could not load existing file {}", opsList.getFile().getName(), var6);
            }
         }

         try {
            List<String> lines = Files.readLines(OLD_OPLIST, StandardCharsets.UTF_8);
            ProfileLookupCallback callback = new OldUsersConverter$2(server, opsList);
            lookupPlayers(server, lines, callback);
            opsList.save();
            renameOldFile(OLD_OPLIST);
            return true;
         } catch (IOException var4) {
            LOGGER.warn("Could not read old oplist to convert it!", var4);
            return false;
         } catch (OldUsersConverter$ConversionError var5) {
            LOGGER.error("Conversion failed, please try again later", var5);
            return false;
         }
      } else {
         return true;
      }
   }

   public static boolean convertWhiteList(final MinecraftServer server) {
      UserWhiteList whitelist = new UserWhiteList(PlayerList.WHITELIST_FILE, new EmptyNotificationService());
      if (OLD_WHITELIST.exists() && OLD_WHITELIST.isFile()) {
         if (whitelist.getFile().exists()) {
            try {
               whitelist.load();
            } catch (IOException var6) {
               LOGGER.warn("Could not load existing file {}", whitelist.getFile().getName(), var6);
            }
         }

         try {
            List<String> lines = Files.readLines(OLD_WHITELIST, StandardCharsets.UTF_8);
            ProfileLookupCallback callback = new OldUsersConverter$3(server, whitelist);
            lookupPlayers(server, lines, callback);
            whitelist.save();
            renameOldFile(OLD_WHITELIST);
            return true;
         } catch (IOException var4) {
            LOGGER.warn("Could not read old whitelist to convert it!", var4);
            return false;
         } catch (OldUsersConverter$ConversionError var5) {
            LOGGER.error("Conversion failed, please try again later", var5);
            return false;
         }
      } else {
         return true;
      }
   }

   @Nullable
   public static UUID convertMobOwnerIfNecessary(final MinecraftServer server, final String owner) {
      if (!StringUtil.isNullOrEmpty(owner) && owner.length() <= 16) {
         Optional<UUID> profileId = server.services().nameToIdCache().get(owner).map(NameAndId::id);
         if (profileId.isPresent()) {
            return profileId.get();
         } else if (!server.isSingleplayer() && server.usesAuthentication()) {
            List<NameAndId> profiles = new ArrayList<>();
            ProfileLookupCallback callback = new OldUsersConverter$4(server, profiles);
            lookupPlayers(server, Lists.newArrayList(new String[]{owner}), callback);
            return !profiles.isEmpty() ? profiles.getFirst().id() : null;
         } else {
            return UUIDUtil.createOfflinePlayerUUID(owner);
         }
      } else {
         try {
            return UUID.fromString(owner);
         } catch (IllegalArgumentException var5) {
            return null;
         }
      }
   }

   public static boolean convertPlayers(final DedicatedServer server) {
      File worldPlayerDirectory = server.getWorldPath(LevelResource.PLAYER_OLD_DATA_DIR).toFile();
      File worldNewPlayerDirectory = new File(worldPlayerDirectory.getParentFile(), LevelResource.PLAYER_DATA_DIR.id());
      File unknownPlayerDirectory = new File(worldPlayerDirectory.getParentFile(), "unknownplayers");
      if (worldPlayerDirectory.exists() && worldPlayerDirectory.isDirectory()) {
         File[] playerFiles = worldPlayerDirectory.listFiles();
         List<String> playerNames = Lists.newArrayList();

         for (File file : playerFiles) {
            String fileName = file.getName();
            if (fileName.toLowerCase(Locale.ROOT).endsWith(".dat")) {
               String playerName = fileName.substring(0, fileName.length() - ".dat".length());
               if (!playerName.isEmpty()) {
                  playerNames.add(playerName);
               }
            }
         }

         try {
            String[] names = playerNames.toArray(new String[playerNames.size()]);
            ProfileLookupCallback callback = new OldUsersConverter$5(server, worldNewPlayerDirectory, unknownPlayerDirectory, worldPlayerDirectory, names);
            lookupPlayers(server, Lists.newArrayList(names), callback);
            return true;
         } catch (OldUsersConverter$ConversionError var12) {
            LOGGER.error("Conversion failed, please try again later", var12);
            return false;
         }
      } else {
         return true;
      }
   }

   private static void ensureDirectoryExists(final File directory) {
      if (directory.exists()) {
         if (!directory.isDirectory()) {
            throw new OldUsersConverter$ConversionError("Can't create directory " + directory.getName() + " in world save directory.");
         }
      } else if (!directory.mkdirs()) {
         throw new OldUsersConverter$ConversionError("Can't create directory " + directory.getName() + " in world save directory.");
      }
   }

   public static boolean areOldUserlistsRemoved() {
      boolean foundUserBanlist = false;
      if (OLD_USERBANLIST.exists() && OLD_USERBANLIST.isFile()) {
         foundUserBanlist = true;
      }

      boolean foundIpBanlist = false;
      if (OLD_IPBANLIST.exists() && OLD_IPBANLIST.isFile()) {
         foundIpBanlist = true;
      }

      boolean foundOpList = false;
      if (OLD_OPLIST.exists() && OLD_OPLIST.isFile()) {
         foundOpList = true;
      }

      boolean foundWhitelist = false;
      if (OLD_WHITELIST.exists() && OLD_WHITELIST.isFile()) {
         foundWhitelist = true;
      }

      if (!foundUserBanlist && !foundIpBanlist && !foundOpList && !foundWhitelist) {
         return true;
      } else {
         LOGGER.warn("**** FAILED TO START THE SERVER AFTER ACCOUNT CONVERSION!");
         LOGGER.warn("** please remove the following files and restart the server:");
         if (foundUserBanlist) {
            LOGGER.warn("* {}", OLD_USERBANLIST.getName());
         }

         if (foundIpBanlist) {
            LOGGER.warn("* {}", OLD_IPBANLIST.getName());
         }

         if (foundOpList) {
            LOGGER.warn("* {}", OLD_OPLIST.getName());
         }

         if (foundWhitelist) {
            LOGGER.warn("* {}", OLD_WHITELIST.getName());
         }

         return false;
      }
   }

   private static void renameOldFile(final File file) {
      File newFile = new File(file.getName() + ".converted");
      file.renameTo(newFile);
   }

   private static Date parseDate(final String dateString, final Date defaultValue) {
      Date parsedDate;
      try {
         parsedDate = BanListEntry.DATE_FORMAT.parse(dateString);
      } catch (ParseException var4) {
         parsedDate = defaultValue;
      }

      return parsedDate;
   }
}
