package net.minecraft.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.illager.IllagerModel;
import net.minecraft.client.renderer.entity.state.IllagerRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.monster.illager.Vindicator;

@Environment(EnvType.CLIENT)
public class VindicatorRenderer extends IllagerRenderer<Vindicator, IllagerRenderState> {
   private static final Identifier VINDICATOR = Identifier.withDefaultNamespace("textures/entity/illager/vindicator.png");

   public VindicatorRenderer(final EntityRendererProvider$Context context) {
      super(context, new IllagerModel<>(context.bakeLayer(ModelLayers.VINDICATOR)), 0.5F);
      this.addLayer(new VindicatorRenderer$1(this, this));
   }

   public Identifier getTextureLocation(final IllagerRenderState state) {
      return VINDICATOR;
   }

   public IllagerRenderState createRenderState() {
      return new IllagerRenderState();
   }
}
