package net.minecraft.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.object.statue.CopperGolemStatueModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public class CopperGolemStatueSpecialRenderer implements NoDataSpecialModelRenderer {
   private final CopperGolemStatueModel model;
   private final Identifier texture;

   public CopperGolemStatueSpecialRenderer(final CopperGolemStatueModel model, final Identifier texture) {
      this.model = model;
      this.texture = texture;
   }

   @Override
   public void submit(
      final PoseStack poseStack,
      final SubmitNodeCollector submitNodeCollector,
      final int lightCoords,
      final int overlayCoords,
      final boolean hasFoil,
      final int outlineColor
   ) {
      submitNodeCollector.submitModel(this.model, Unit.INSTANCE, poseStack, this.texture, lightCoords, overlayCoords, outlineColor);
   }

   @Override
   public void getExtents(final Consumer<Vector3fc> output) {
      PoseStack poseStack = new PoseStack();
      this.model.setupAnim(Unit.INSTANCE);
      this.model.root().getExtentsForGui(poseStack, output);
   }
}
