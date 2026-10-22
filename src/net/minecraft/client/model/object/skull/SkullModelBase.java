package net.minecraft.client.model.object.skull;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderTypes;

@Environment(EnvType.CLIENT)
public abstract class SkullModelBase extends Model<SkullModelBase$State> {
   public SkullModelBase(final ModelPart root) {
      super(root, RenderTypes::entityTranslucent);
   }
}
