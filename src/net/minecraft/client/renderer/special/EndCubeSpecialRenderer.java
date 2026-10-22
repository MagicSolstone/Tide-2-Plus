package net.minecraft.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public class EndCubeSpecialRenderer implements NoDataSpecialModelRenderer {
   private final RenderType renderType;

   public EndCubeSpecialRenderer(final RenderType renderType) {
      this.renderType = renderType;
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
      AbstractEndPortalRenderer.submitSpecial(this.renderType, poseStack, submitNodeCollector, outlineColor);
   }

   @Override
   public void getExtents(final Consumer<Vector3fc> output) {
      AbstractEndPortalRenderer.getExtents(output);
   }
}
