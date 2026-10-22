package net.minecraft.client.renderer;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.server.players.ProfileResolver;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.item.component.ResolvableProfile;

@Environment(EnvType.CLIENT)
public class PlayerSkinRenderCache {
   public static final RenderType DEFAULT_PLAYER_SKIN_RENDER_TYPE = playerSkinRenderType(DefaultPlayerSkin.getDefaultSkin());
   public static final Duration CACHE_DURATION = Duration.ofMinutes(5L);
   private final LoadingCache<ResolvableProfile, CompletableFuture<Optional<PlayerSkinRenderCache$RenderInfo>>> renderInfoCache = CacheBuilder.newBuilder()
      .expireAfterAccess(CACHE_DURATION)
      .build(new PlayerSkinRenderCache$1(this));
   private final LoadingCache<ResolvableProfile, PlayerSkinRenderCache$RenderInfo> defaultSkinCache = CacheBuilder.newBuilder()
      .expireAfterAccess(CACHE_DURATION)
      .build(new PlayerSkinRenderCache$2(this));
   private final TextureManager textureManager;
   private final SkinManager skinManager;
   private final ProfileResolver profileResolver;

   public PlayerSkinRenderCache(final TextureManager textureManager, final SkinManager skinManager, final ProfileResolver profileResolver) {
      this.textureManager = textureManager;
      this.skinManager = skinManager;
      this.profileResolver = profileResolver;
   }

   public PlayerSkinRenderCache$RenderInfo getOrDefault(final ResolvableProfile profile) {
      PlayerSkinRenderCache$RenderInfo result = this.lookup(profile).getNow(Optional.empty()).orElse(null);
      return result != null ? result : (PlayerSkinRenderCache$RenderInfo)this.defaultSkinCache.getUnchecked(profile);
   }

   public Supplier<PlayerSkinRenderCache$RenderInfo> createLookup(final ResolvableProfile profile) {
      PlayerSkinRenderCache$RenderInfo defaultForProfile = (PlayerSkinRenderCache$RenderInfo)this.defaultSkinCache.getUnchecked(profile);
      CompletableFuture<Optional<PlayerSkinRenderCache$RenderInfo>> future = (CompletableFuture<Optional<PlayerSkinRenderCache$RenderInfo>>)this.renderInfoCache
         .getUnchecked(profile);
      Optional<PlayerSkinRenderCache$RenderInfo> currentValue = future.getNow(null);
      if (currentValue != null) {
         PlayerSkinRenderCache$RenderInfo finalValue = currentValue.orElse(defaultForProfile);
         return () -> finalValue;
      } else {
         return () -> future.getNow(Optional.empty()).orElse(defaultForProfile);
      }
   }

   public CompletableFuture<Optional<PlayerSkinRenderCache$RenderInfo>> lookup(final ResolvableProfile profile) {
      return (CompletableFuture<Optional<PlayerSkinRenderCache$RenderInfo>>)this.renderInfoCache.getUnchecked(profile);
   }

   private static RenderType playerSkinRenderType(final PlayerSkin playerSkin) {
      return SkullBlockRenderer.getPlayerSkinRenderType(playerSkin.body().texturePath());
   }
}
