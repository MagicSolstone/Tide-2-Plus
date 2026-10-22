package net.minecraft.client.model.animal.feline;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.FelineRenderState;

@Environment(EnvType.CLIENT)
public class AdultOcelotModel extends AdultFelineModel<FelineRenderState> {
   public AdultOcelotModel(final ModelPart root) {
      super(root);
   }
}
