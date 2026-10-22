package net.minecraft.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Transformation;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.object.projectile.TridentModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Unit;
import org.joml.Vector3f;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public class TridentSpecialRenderer implements NoDataSpecialModelRenderer {
   public static final Transformation DEFAULT_TRANSFORMATION = new Transformation(null, null, new Vector3f(1.0F, -1.0F, -1.0F), null);
   private final TridentModel model;

   public TridentSpecialRenderer(final TridentModel model) {
      this.model = model;
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
      if (hasFoil) {
         submitNodeCollector.submitModel(
            this.model, Unit.INSTANCE, poseStack, RenderTypes.entitySolidGlint(TridentModel.TEXTURE), lightCoords, overlayCoords, outlineColor
         );
      } else {
         submitNodeCollector.submitModel(this.model, Unit.INSTANCE, poseStack, TridentModel.TEXTURE, lightCoords, overlayCoords, outlineColor);
      }
   }

   @Override
   public void getExtents(final Consumer<Vector3fc> output) {
      PoseStack poseStack = new PoseStack();
      this.model.root().getExtentsForGui(poseStack, output);
   }
}
