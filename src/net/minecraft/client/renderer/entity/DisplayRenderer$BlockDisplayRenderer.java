package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.BlockDisplayEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Display$BlockDisplay;
import net.minecraft.world.entity.Display$BlockDisplay$BlockRenderState;

@Environment(EnvType.CLIENT)
public class DisplayRenderer$BlockDisplayRenderer
   extends DisplayRenderer<Display$BlockDisplay, Display$BlockDisplay$BlockRenderState, BlockDisplayEntityRenderState> {
   protected DisplayRenderer$BlockDisplayRenderer(final EntityRendererProvider$Context context) {
      super(context);
   }

   public BlockDisplayEntityRenderState createRenderState() {
      return new BlockDisplayEntityRenderState();
   }

   public void extractRenderState(final Display$BlockDisplay entity, final BlockDisplayEntityRenderState state, final float partialTicks) {
      super.extractRenderState(entity, state, partialTicks);
      Display$BlockDisplay$BlockRenderState blockRenderState = entity.blockRenderState();
      if (blockRenderState != null) {
         this.blockModelResolver.update(state.blockModel, blockRenderState.blockState(), BLOCK_DISPLAY_CONTEXT);
      } else {
         state.blockModel.clear();
      }
   }

   public void submitInner(
      final BlockDisplayEntityRenderState state,
      final PoseStack poseStack,
      final SubmitNodeCollector submitNodeCollector,
      final int lightCoords,
      final float interpolationProgress
   ) {
      state.blockModel.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
   }
}
