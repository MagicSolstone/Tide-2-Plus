package net.minecraft.client.renderer;

import com.google.common.cache.CacheLoader;
import com.mojang.authlib.GameProfile;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.world.item.component.ResolvableProfile;

@Environment(EnvType.CLIENT)
class PlayerSkinRenderCache$2 extends CacheLoader<ResolvableProfile, PlayerSkinRenderCache$RenderInfo> {
   PlayerSkinRenderCache$2(final PlayerSkinRenderCache this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   public PlayerSkinRenderCache$RenderInfo load(final ResolvableProfile profile) {
      GameProfile temporaryProfile = profile.partialProfile();
      return new PlayerSkinRenderCache$RenderInfo(this.this$0, temporaryProfile, DefaultPlayerSkin.get(temporaryProfile), profile.skinPatch());
   }
}
