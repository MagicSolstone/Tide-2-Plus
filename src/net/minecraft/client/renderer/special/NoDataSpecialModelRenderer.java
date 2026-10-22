package net.minecraft.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface NoDataSpecialModelRenderer extends SpecialModelRenderer<Void> {
   @Nullable
   default Void extractArgument(final ItemStack stack) {
      return null;
   }

   default void submit(
      @Nullable final Void argument,
      final PoseStack poseStack,
      final SubmitNodeCollector submitNodeCollector,
      final int lightCoords,
      final int overlayCoords,
      final boolean hasFoil,
      final int outlineColor
   ) {
      this.submit(poseStack, submitNodeCollector, lightCoords, overlayCoords, hasFoil, outlineColor);
   }

   void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, final int outlineColor);
}
