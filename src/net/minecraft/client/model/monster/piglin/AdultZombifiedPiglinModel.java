package net.minecraft.client.model.monster.piglin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;

@Environment(EnvType.CLIENT)
public class AdultZombifiedPiglinModel extends ZombifiedPiglinModel {
   public AdultZombifiedPiglinModel(final ModelPart root) {
      super(root);
   }

   @Override
   protected float getDefaultEarAngleInDegrees() {
      return 30.0F;
   }

   public static LayerDefinition createBodyLayer() {
      return AdultPiglinModel.createBodyLayer();
   }
}
