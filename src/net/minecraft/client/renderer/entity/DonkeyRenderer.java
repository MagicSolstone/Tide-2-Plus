package net.minecraft.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.animal.equine.BabyDonkeyModel;
import net.minecraft.client.model.animal.equine.DonkeyModel;
import net.minecraft.client.model.animal.equine.EquineSaddleModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
import net.minecraft.client.renderer.entity.state.DonkeyRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo$LayerType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;

@Environment(EnvType.CLIENT)
public class DonkeyRenderer<T extends AbstractChestedHorse> extends AbstractHorseRenderer<T, DonkeyRenderState, DonkeyModel> {
   private final Identifier adultTexture;
   private final Identifier babyTexture;

   public DonkeyRenderer(
      final EntityRendererProvider$Context context,
      final EquipmentClientInfo$LayerType saddleLayer,
      final ModelLayerLocation saddleModel,
      final DonkeyRenderer$Type adult,
      final DonkeyRenderer$Type baby
   ) {
      super(context, new DonkeyModel(context.bakeLayer(adult.model)), new BabyDonkeyModel(context.bakeLayer(baby.model)));
      this.adultTexture = adult.texture;
      this.babyTexture = baby.texture;
      this.addLayer(
         new SimpleEquipmentLayer<>(
            this, context.getEquipmentRenderer(), saddleLayer, state -> state.saddle, new EquineSaddleModel(context.bakeLayer(saddleModel)), null
         )
      );
   }

   public Identifier getTextureLocation(final DonkeyRenderState state) {
      return state.isBaby ? this.babyTexture : this.adultTexture;
   }

   public DonkeyRenderState createRenderState() {
      return new DonkeyRenderState();
   }

   public void extractRenderState(final T entity, final DonkeyRenderState state, final float partialTicks) {
      super.extractRenderState(entity, state, partialTicks);
      state.hasChest = entity.hasChest();
   }
}
