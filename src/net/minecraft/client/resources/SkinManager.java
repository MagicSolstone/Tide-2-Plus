package net.minecraft.client.resources;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.SignatureState;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTextures;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.authlib.properties.Property;
import com.mojang.logging.LogUtils;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.renderer.texture.SkinTextureDownloader;
import net.minecraft.core.ClientAsset$Texture;
import net.minecraft.server.Services;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class SkinManager {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final Services services;
   private final SkinTextureDownloader skinTextureDownloader;
   private final LoadingCache<SkinManager$CacheKey, CompletableFuture<Optional<PlayerSkin>>> skinCache;
   private final SkinManager$TextureCache skinTextures;
   private final SkinManager$TextureCache capeTextures;
   private final SkinManager$TextureCache elytraTextures;

   public SkinManager(final Path skinsDirectory, final Services services, final SkinTextureDownloader skinTextureDownloader, final Executor mainThreadExecutor) {
      this.services = services;
      this.skinTextureDownloader = skinTextureDownloader;
      this.skinTextures = new SkinManager$TextureCache(this, skinsDirectory, Type.SKIN);
      this.capeTextures = new SkinManager$TextureCache(this, skinsDirectory, Type.CAPE);
      this.elytraTextures = new SkinManager$TextureCache(this, skinsDirectory, Type.ELYTRA);
      this.skinCache = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofSeconds(15L)).build(new SkinManager$1(this, services, mainThreadExecutor));
   }

   public Supplier<PlayerSkin> createLookup(final GameProfile profile, final boolean requireSecure) {
      CompletableFuture<Optional<PlayerSkin>> future = this.get(profile);
      PlayerSkin defaultSkin = DefaultPlayerSkin.get(profile);
      if (SharedConstants.DEBUG_DEFAULT_SKIN_OVERRIDE) {
         return () -> defaultSkin;
      } else {
         Optional<PlayerSkin> currentValue = future.getNow(null);
         if (currentValue != null) {
            PlayerSkin playerSkin = currentValue.filter(skin -> !requireSecure || skin.secure()).orElse(defaultSkin);
            return () -> playerSkin;
         } else {
            return () -> future.getNow(Optional.empty()).filter(skin -> !requireSecure || skin.secure()).orElse(defaultSkin);
         }
      }
   }

   public CompletableFuture<Optional<PlayerSkin>> get(final GameProfile profile) {
      if (SharedConstants.DEBUG_DEFAULT_SKIN_OVERRIDE) {
         PlayerSkin defaultSkin = DefaultPlayerSkin.get(profile);
         return CompletableFuture.completedFuture(Optional.of(defaultSkin));
      } else {
         Property packedTextures = this.services.sessionService().getPackedTextures(profile);
         return (CompletableFuture<Optional<PlayerSkin>>)this.skinCache.getUnchecked(new SkinManager$CacheKey(profile.id(), packedTextures));
      }
   }

   private CompletableFuture<PlayerSkin> registerTextures(final UUID profileId, final MinecraftProfileTextures textures) {
      MinecraftProfileTexture skinInfo = textures.skin();
      CompletableFuture<ClientAsset$Texture> skinTexture;
      PlayerModelType model;
      if (skinInfo != null) {
         skinTexture = this.skinTextures.getOrLoad(skinInfo);
         model = PlayerModelType.byLegacyServicesName(skinInfo.getMetadata("model"));
      } else {
         PlayerSkin defaultSkin = DefaultPlayerSkin.get(profileId);
         skinTexture = CompletableFuture.completedFuture(defaultSkin.body());
         model = defaultSkin.model();
      }

      MinecraftProfileTexture capeInfo = textures.cape();
      CompletableFuture<ClientAsset$Texture> capeTexture = capeInfo != null ? this.capeTextures.getOrLoad(capeInfo) : CompletableFuture.completedFuture(null);
      MinecraftProfileTexture elytraInfo = textures.elytra();
      CompletableFuture<ClientAsset$Texture> elytraTexture = elytraInfo != null
         ? this.elytraTextures.getOrLoad(elytraInfo)
         : CompletableFuture.completedFuture(null);
      return CompletableFuture.allOf(skinTexture, capeTexture, elytraTexture)
         .thenApply(
            unused -> new PlayerSkin(skinTexture.join(), capeTexture.join(), elytraTexture.join(), model, textures.signatureState() == SignatureState.SIGNED)
         );
   }
}
