package net.minecraft.client.model.animal.feline;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.MeshTransformer;
import net.minecraft.client.renderer.entity.state.CatRenderState;

@Environment(EnvType.CLIENT)
public class BabyCatModel extends BabyFelineModel<CatRenderState> {
   public static final MeshTransformer COLLAR_TRANSFORMER = MeshTransformer.scaling(1.01F);

   public BabyCatModel(final ModelPart root) {
      super(root);
   }
}
