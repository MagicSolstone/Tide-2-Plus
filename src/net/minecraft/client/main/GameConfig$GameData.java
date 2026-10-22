package net.minecraft.client.main;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.PreferredGraphicsApi;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class GameConfig$GameData {
   public final boolean demo;
   public final String launchVersion;
   public final String versionType;
   public final boolean disableMultiplayer;
   public final boolean disableChat;
   public final boolean captureTracyImages;
   public final boolean renderDebugLabels;
   public final boolean vulkanValidation;
   @Nullable
   public final PreferredGraphicsApi forcedGraphicsApi;
   public final boolean offlineDeveloperMode;

   public GameConfig$GameData(
      final boolean demo,
      final String launchVersion,
      final String versionType,
      final boolean disableMultiplayer,
      final boolean disableChat,
      final boolean captureTracyImages,
      final boolean vulkanValidation,
      final boolean renderDebugLabels,
      @Nullable final PreferredGraphicsApi forcedGraphicsApi,
      final boolean offlineDeveloperMode
   ) {
      this.demo = demo;
      this.launchVersion = launchVersion;
      this.versionType = versionType;
      this.disableMultiplayer = disableMultiplayer;
      this.disableChat = disableChat;
      this.captureTracyImages = captureTracyImages;
      this.vulkanValidation = vulkanValidation;
      this.renderDebugLabels = renderDebugLabels;
      this.forcedGraphicsApi = forcedGraphicsApi;
      this.offlineDeveloperMode = offlineDeveloperMode;
   }
}
