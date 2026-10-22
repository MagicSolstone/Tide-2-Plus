package net.minecraft.client.renderer.special;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.bell.BellModel;

@Environment(EnvType.CLIENT)
public record BellSpecialRenderer$Unbaked() implements NoDataSpecialModelRenderer$Unbaked {
   public static final MapCodec<BellSpecialRenderer$Unbaked> MAP_CODEC = MapCodec.unit(new BellSpecialRenderer$Unbaked());

   @Override
   public MapCodec<BellSpecialRenderer$Unbaked> type() {
      return MAP_CODEC;
   }

   public BellSpecialRenderer bake(final SpecialModelRenderer$BakingContext context) {
      return new BellSpecialRenderer(context.sprites(), new BellModel(context.entityModelSet().bakeLayer(ModelLayers.BELL)));
   }
}
