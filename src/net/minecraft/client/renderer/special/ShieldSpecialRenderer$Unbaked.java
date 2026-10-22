package net.minecraft.client.renderer.special;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.equipment.ShieldModel;
import net.minecraft.core.component.DataComponentMap;

@Environment(EnvType.CLIENT)
public record ShieldSpecialRenderer$Unbaked() implements SpecialModelRenderer$Unbaked<DataComponentMap> {
   public static final ShieldSpecialRenderer$Unbaked INSTANCE = new ShieldSpecialRenderer$Unbaked();
   public static final MapCodec<ShieldSpecialRenderer$Unbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

   @Override
   public MapCodec<ShieldSpecialRenderer$Unbaked> type() {
      return MAP_CODEC;
   }

   public ShieldSpecialRenderer bake(final SpecialModelRenderer$BakingContext context) {
      return new ShieldSpecialRenderer(context.sprites(), new ShieldModel(context.entityModelSet().bakeLayer(ModelLayers.SHIELD)));
   }
}
