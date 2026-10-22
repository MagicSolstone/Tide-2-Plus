package net.minecraft.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;

@Environment(EnvType.CLIENT)
public class MinecartRenderer extends AbstractMinecartRenderer<AbstractMinecart, MinecartRenderState> {
   public MinecartRenderer(final EntityRendererProvider$Context context, final ModelLayerLocation model) {
      super(context, model);
   }

   public MinecartRenderState createRenderState() {
      return new MinecartRenderState();
   }
}
