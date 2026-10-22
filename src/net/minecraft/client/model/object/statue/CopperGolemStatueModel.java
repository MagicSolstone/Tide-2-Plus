package net.minecraft.client.model.object.statue;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Unit;

@Environment(EnvType.CLIENT)
public class CopperGolemStatueModel extends Model<Unit> {
   public CopperGolemStatueModel(final ModelPart root) {
      super(root, RenderTypes::entityCutout);
   }

   public void setupAnim(final Unit ignored) {
      this.root.y = 0.0F;
      this.root.zRot = (float) Math.PI;
   }
}
