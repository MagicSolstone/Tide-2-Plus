package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.ItemDisplayEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Display$ItemDisplay;
import net.minecraft.world.entity.Display$ItemDisplay$ItemRenderState;

@Environment(EnvType.CLIENT)
public class DisplayRenderer$ItemDisplayRenderer
   extends DisplayRenderer<Display$ItemDisplay, Display$ItemDisplay$ItemRenderState, ItemDisplayEntityRenderState> {
   private final ItemModelResolver itemModelResolver;

   protected DisplayRenderer$ItemDisplayRenderer(final EntityRendererProvider$Context context) {
      super(context);
      this.itemModelResolver = context.getItemModelResolver();
   }

   public ItemDisplayEntityRenderState createRenderState() {
      return new ItemDisplayEntityRenderState();
   }

   public void extractRenderState(final Display$ItemDisplay entity, final ItemDisplayEntityRenderState state, final float partialTicks) {
      super.extractRenderState(entity, state, partialTicks);
      Display$ItemDisplay$ItemRenderState itemRenderState = entity.itemRenderState();
      if (itemRenderState != null) {
         this.itemModelResolver.updateForNonLiving(state.item, itemRenderState.itemStack(), itemRenderState.itemTransform(), entity);
      } else {
         state.item.clear();
      }
   }

   public void submitInner(
      final ItemDisplayEntityRenderState state,
      final PoseStack poseStack,
      final SubmitNodeCollector submitNodeCollector,
      final int lightCoords,
      final float interpolationProgress
   ) {
      if (!state.item.isEmpty()) {
         poseStack.rotate(Axis.YP, (float) Math.PI);
         state.item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
      }
   }
}
