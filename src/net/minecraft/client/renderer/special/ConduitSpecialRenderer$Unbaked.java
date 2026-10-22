package net.minecraft.client.renderer.special;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayers;

@Environment(EnvType.CLIENT)
public record ConduitSpecialRenderer$Unbaked() implements NoDataSpecialModelRenderer$Unbaked {
   public static final MapCodec<ConduitSpecialRenderer$Unbaked> MAP_CODEC = MapCodec.unit(new ConduitSpecialRenderer$Unbaked());

   @Override
   public MapCodec<ConduitSpecialRenderer$Unbaked> type() {
      return MAP_CODEC;
   }

   public ConduitSpecialRenderer bake(final SpecialModelRenderer$BakingContext context) {
      return new ConduitSpecialRenderer(context.sprites(), context.entityModelSet().bakeLayer(ModelLayers.CONDUIT_SHELL));
   }
}
