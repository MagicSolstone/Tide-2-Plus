package net.minecraft.client.gui.screens.social;

import com.mojang.authlib.services.FriendsService;
import com.mojang.authlib.services.response.PresenceResponse;
import com.mojang.authlib.services.response.PresenceStatus;
import com.mojang.logging.LogUtils;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.PresenceSharing;
import net.minecraft.client.gui.screens.friends.FriendsOverlayScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.MinecraftServer$MultiplayerScope;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class PresenceHandler {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final Duration PRESENCE_UPDATE_INTERVAL = Duration.ofMinutes(1L);
   private static final long MAX_PRESENCE_INTERVAL_MULTIPLIER = 5L;
   private final Minecraft minecraft;
   private final FriendsService friendsService;
   private PresenceResponse latestPresence = new PresenceResponse(new ArrayList());
   private Instant lastPresencePost = Instant.now();
   private boolean updatePresence;

   public PresenceHandler(final Minecraft minecraft, final FriendsService friendsService) {
      this.minecraft = minecraft;
      this.friendsService = friendsService;
      this.updatePresence = true;
   }

   private void updatePresence() {
      this.updatePresence = false;
      this.lastPresencePost = Instant.now();
      PresenceStatus publicPresenceStatus = this.getPublicPresenceStatus();
      CompletableFuture.runAsync(() -> {
         PresenceResponse newPresence = this.postPresence(publicPresenceStatus);
         if (newPresence != null) {
            this.minecraft.execute(() -> {
               boolean refreshPresence = !Objects.equals(this.latestPresence, newPresence);
               this.latestPresence = newPresence;
               if (refreshPresence && this.minecraft.gui.screen() instanceof FriendsOverlayScreen friendsOverlayScreen) {
                  friendsOverlayScreen.applyPresenceUpdate();
               }
            });
         }
      }, Util.nonCriticalIoPool());
   }

   @Nullable
   private PresenceResponse postPresence(final PresenceStatus status) {
      try {
         return this.friendsService.presence(status.name());
      } catch (Exception var3) {
         LOGGER.warn("Failed to post presence {}", status, var3);
         return null;
      }
   }

   private boolean isPresenceSharingDisabled() {
      PlayerSocialManager socialManager = this.minecraft.getPlayerSocialManager();
      return !socialManager.isFriendListEnabled() || socialManager.getFriends().isEmpty();
   }

   private boolean shouldRefreshPresence() {
      if (this.isPresenceSharingDisabled()) {
         return false;
      } else {
         Duration sinceLastPresence = Duration.between(this.lastPresencePost, Instant.now());
         Optional<Duration> presencePollInterval = this.friendsService.getPresencePollInterval();
         Duration interval = !presencePollInterval.isEmpty() && presencePollInterval.get().isPositive() ? presencePollInterval.get() : PRESENCE_UPDATE_INTERVAL;
         Duration maxInterval = interval.multipliedBy(5L);
         return this.updatePresence && sinceLastPresence.compareTo(interval) >= 0 || sinceLastPresence.compareTo(maxInterval) >= 0;
      }
   }

   public void tick() {
      if (this.shouldRefreshPresence()) {
         this.updatePresence();
      }
   }

   public void tryUpdatePresence() {
      this.updatePresence = true;
   }

   public void sendOfflinePresence() {
      if (!this.isPresenceSharingDisabled()) {
         Util.ioPool().execute(() -> this.postPresence(PresenceStatus.OFFLINE));
      }
   }

   public PresenceResponse getLatestPresence() {
      return this.latestPresence;
   }

   private PresenceStatus getPublicPresenceStatus() {
      return switch ((PresenceSharing)this.minecraft.options.sharePresence().get()) {
         case NONE -> PresenceStatus.OFFLINE;
         case LIMITED -> PresenceStatus.ONLINE;
         case ALL -> this.getPresenceStatus();
      };
   }

   private PresenceStatus getPresenceStatus() {
      IntegratedServer singleplayerServer = this.minecraft.getSingleplayerServer();
      if (singleplayerServer != null) {
         return singleplayerServer.getMultiplayerScope() == MinecraftServer$MultiplayerScope.LAN
            ? PresenceStatus.PLAYING_HOSTED_SERVER
            : PresenceStatus.PLAYING_OFFLINE;
      } else {
         ServerData server = this.minecraft.getCurrentServer();
         if (server != null) {
            return server.isRealm() ? PresenceStatus.PLAYING_REALMS : PresenceStatus.PLAYING_SERVER;
         } else {
            return PresenceStatus.ONLINE;
         }
      }
   }
}
