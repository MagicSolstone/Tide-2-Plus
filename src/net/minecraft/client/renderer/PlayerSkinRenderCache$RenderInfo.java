package net.minecraft.client.renderer;

import com.mojang.authlib.GameProfile;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.GlyphRenderTypes;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.entity.player.PlayerSkin$Patch;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class PlayerSkinRenderCache$RenderInfo {
   private final GameProfile gameProfile;
   private final PlayerSkin playerSkin;
   @Nullable
   private RenderType itemRenderType;
   @Nullable
   private GpuTextureView textureView;
   @Nullable
   private GlyphRenderTypes glyphRenderTypes;

   public PlayerSkinRenderCache$RenderInfo(
      final PlayerSkinRenderCache this$0, final GameProfile gameProfile, final PlayerSkin playerSkin, final PlayerSkin$Patch patch
   ) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.gameProfile = gameProfile;
      this.playerSkin = playerSkin.with(patch);
   }

   public GameProfile gameProfile() {
      return this.gameProfile;
   }

   public PlayerSkin playerSkin() {
      return this.playerSkin;
   }

   public RenderType renderType() {
      if (this.itemRenderType == null) {
         this.itemRenderType = PlayerSkinRenderCache.playerSkinRenderType(this.playerSkin);
      }

      return this.itemRenderType;
   }

   public GpuTextureView textureView() {
      if (this.textureView == null) {
         this.textureView = this.this$0.textureManager.getTexture(this.playerSkin.body().texturePath()).getTextureView();
      }

      return this.textureView;
   }

   public GlyphRenderTypes glyphRenderTypes() {
      if (this.glyphRenderTypes == null) {
         this.glyphRenderTypes = GlyphRenderTypes.createForColorTexture(this.playerSkin.body().texturePath());
      }

      return this.glyphRenderTypes;
   }

   @Override
   public boolean equals(final Object o) {
      return this == o
         || o instanceof PlayerSkinRenderCache$RenderInfo that && this.gameProfile.equals(that.gameProfile) && this.playerSkin.equals(that.playerSkin);
   }

   @Override
   public int hashCode() {
      int result = 1;
      result = 31 * result + this.gameProfile.hashCode();
      return 31 * result + this.playerSkin.hashCode();
   }
}
