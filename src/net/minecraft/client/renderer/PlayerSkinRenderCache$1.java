package net.minecraft.client.renderer;

import com.google.common.cache.CacheLoader;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.component.ResolvableProfile;

@Environment(EnvType.CLIENT)
class PlayerSkinRenderCache$1 extends CacheLoader<ResolvableProfile, CompletableFuture<Optional<PlayerSkinRenderCache$RenderInfo>>> {
   PlayerSkinRenderCache$1(final PlayerSkinRenderCache this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   public CompletableFuture<Optional<PlayerSkinRenderCache$RenderInfo>> load(final ResolvableProfile profile) {
      return profile.resolveProfile(this.this$0.profileResolver)
         .thenCompose(
            resolvedProfile -> this.this$0
               .skinManager
               .get(resolvedProfile)
               .thenApply(playerSkin -> playerSkin.map(skin -> new PlayerSkinRenderCache$RenderInfo(this.this$0, resolvedProfile, skin, profile.skinPatch())))
         );
   }
}
