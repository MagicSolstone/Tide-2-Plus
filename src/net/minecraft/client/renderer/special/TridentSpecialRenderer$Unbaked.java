package net.minecraft.client.renderer.special;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.projectile.TridentModel;

@Environment(EnvType.CLIENT)
public record TridentSpecialRenderer$Unbaked() implements NoDataSpecialModelRenderer$Unbaked {
   public static final MapCodec<TridentSpecialRenderer$Unbaked> MAP_CODEC = MapCodec.unit(new TridentSpecialRenderer$Unbaked());

   @Override
   public MapCodec<TridentSpecialRenderer$Unbaked> type() {
      return MAP_CODEC;
   }

   public TridentSpecialRenderer bake(final SpecialModelRenderer$BakingContext context) {
      return new TridentSpecialRenderer(new TridentModel(context.entityModelSet().bakeLayer(ModelLayers.TRIDENT)));
   }
}
