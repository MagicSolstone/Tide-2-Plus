package net.minecraft.client.renderer.blockentity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderTypes;

@Environment(EnvType.CLIENT)
class ShulkerBoxRenderer$ShulkerBoxModel extends Model<Float> {
   private final ModelPart lid;

   public ShulkerBoxRenderer$ShulkerBoxModel(final ModelPart root) {
      super(root, RenderTypes::entityCutout);
      this.lid = root.getChild("lid");
   }

   public void setupAnim(final Float progress) {
      super.setupAnim(progress);
      this.lid.setPos(0.0F, 24.0F - progress * 0.5F * 16.0F, 0.0F);
      this.lid.yRot = 270.0F * progress * (float) (Math.PI / 180.0);
   }
}
