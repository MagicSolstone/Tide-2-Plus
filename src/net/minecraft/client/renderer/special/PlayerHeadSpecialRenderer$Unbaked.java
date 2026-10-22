package net.minecraft.client.renderer.special;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.skull.SkullModel;
import net.minecraft.client.renderer.PlayerSkinRenderCache$RenderInfo;

@Environment(EnvType.CLIENT)
public record PlayerHeadSpecialRenderer$Unbaked() implements SpecialModelRenderer$Unbaked<PlayerSkinRenderCache$RenderInfo> {
   public static final MapCodec<PlayerHeadSpecialRenderer$Unbaked> MAP_CODEC = MapCodec.unit(PlayerHeadSpecialRenderer$Unbaked::new);

   @Override
   public MapCodec<PlayerHeadSpecialRenderer$Unbaked> type() {
      return MAP_CODEC;
   }

   public PlayerHeadSpecialRenderer bake(final SpecialModelRenderer$BakingContext context) {
      SkullModel model = new SkullModel(context.entityModelSet().bakeLayer(ModelLayers.PLAYER_HEAD));
      return new PlayerHeadSpecialRenderer(context.playerSkinRenderCache(), model);
   }
}
