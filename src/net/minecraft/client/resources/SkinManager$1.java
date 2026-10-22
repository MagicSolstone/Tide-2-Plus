package net.minecraft.client.resources;

import com.google.common.cache.CacheLoader;
import com.mojang.authlib.SignatureState;
import com.mojang.authlib.minecraft.MinecraftProfileTextures;
import com.mojang.authlib.properties.Property;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.Services;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.PlayerSkin;

@Environment(EnvType.CLIENT)
class SkinManager$1 extends CacheLoader<SkinManager$CacheKey, CompletableFuture<Optional<PlayerSkin>>> {
   SkinManager$1(final SkinManager this$0, final Services val$services, final Executor val$mainThreadExecutor) {
      this.val$services = val$servicesx;
      this.val$mainThreadExecutor = val$mainThreadExecutorx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   public CompletableFuture<Optional<PlayerSkin>> load(final SkinManager$CacheKey key) {
      return CompletableFuture.<MinecraftProfileTextures>supplyAsync(() -> {
            Property packedTextures = key.packedTextures();
            if (packedTextures == null) {
               return MinecraftProfileTextures.EMPTY;
            } else {
               MinecraftProfileTextures textures = services.sessionService().unpackTextures(packedTextures);
               if (textures.signatureState() == SignatureState.INVALID) {
                  SkinManager.LOGGER.warn("Profile contained invalid signature for textures property (profile id: {})", key.profileId());
               }

               return textures;
            }
         }, Util.backgroundExecutor().forName("unpackSkinTextures"))
         .thenComposeAsync(textures -> this.this$0.registerTextures(key.profileId(), textures), this.val$mainThreadExecutor)
         .handle((playerSkin, throwable) -> {
            if (throwable != null) {
               SkinManager.LOGGER.warn("Failed to load texture for profile {}", key.profileId, throwable);
            }

            return Optional.ofNullable(playerSkin);
         });
   }
}
