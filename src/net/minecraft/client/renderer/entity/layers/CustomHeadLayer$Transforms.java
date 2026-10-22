package net.minecraft.client.renderer.entity.layers;

import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.PlayerSkinRenderCache$RenderInfo;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;

@Environment(EnvType.CLIENT)
public record CustomHeadLayer$Transforms(
   float yOffset,
   float skullYOffset,
   float horizontalScale,
   float verticalScale,
   Function<PlayerSkinRenderCache$RenderInfo, RenderType> playerSkinRenderTypeResolver
) {
   public static final Function<PlayerSkinRenderCache$RenderInfo, RenderType> TRANSLUCENT_PLAYER_SKIN_RESOLVER = PlayerSkinRenderCache$RenderInfo::renderType;
   public static final Function<PlayerSkinRenderCache$RenderInfo, RenderType> CUTOUT_PLAYER_SKIN_RESOLVER = renderInfo -> SkullBlockRenderer.getPlayerSkinRenderTypeCutout(
      renderInfo.playerSkin().body().texturePath()
   );
   public static final CustomHeadLayer$Transforms DEFAULT = new CustomHeadLayer$Transforms(0.0F, 0.0F, 1.0F, 1.0F);

   public CustomHeadLayer$Transforms(final float yOffset, final float skullYOffset, final float horizontalScale, final float verticalScale) {
      this(yOffset, skullYOffset, horizontalScale, verticalScale, TRANSLUCENT_PLAYER_SKIN_RESOLVER);
   }

   public CustomHeadLayer$Transforms(final float yOffset, final float skullYOffset, final float horizontalScale) {
      this(yOffset, skullYOffset, horizontalScale, 1.0F);
   }
}
