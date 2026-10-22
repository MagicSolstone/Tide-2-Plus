package net.minecraft.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.object.boat.RaftModel;
import net.minecraft.client.renderer.entity.state.BoatRenderState;

@Environment(EnvType.CLIENT)
public class RaftRenderer extends AbstractBoatRenderer {
   private final EntityModel<BoatRenderState> model;

   public RaftRenderer(final EntityRendererProvider$Context context, final ModelLayerLocation modelId) {
      super(context, modelId.model().withPath(p -> "textures/entity/" + p + ".png"));
      this.model = new RaftModel(context.bakeLayer(modelId));
   }

   @Override
   protected EntityModel<BoatRenderState> model() {
      return this.model;
   }
}
