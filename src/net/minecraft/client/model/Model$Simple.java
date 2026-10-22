package net.minecraft.client.model;

import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;

@Environment(EnvType.CLIENT)
public class Model$Simple extends Model<Unit> {
   public Model$Simple(final ModelPart root, final Function<Identifier, RenderType> renderType) {
      super(root, renderType);
   }

   public void setupAnim(final Unit state) {
   }
}
