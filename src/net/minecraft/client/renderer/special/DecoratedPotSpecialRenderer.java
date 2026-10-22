package net.minecraft.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Objects;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.DecoratedPotRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.PotDecorations;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class DecoratedPotSpecialRenderer implements SpecialModelRenderer<PotDecorations> {
   private final DecoratedPotRenderer decoratedPotRenderer;

   public DecoratedPotSpecialRenderer(final DecoratedPotRenderer decoratedPotRenderer) {
      this.decoratedPotRenderer = decoratedPotRenderer;
   }

   @Nullable
   public PotDecorations extractArgument(final ItemStack stack) {
      return stack.get(DataComponents.POT_DECORATIONS);
   }

   public void submit(
      @Nullable final PotDecorations decorations,
      final PoseStack poseStack,
      final SubmitNodeCollector submitNodeCollector,
      final int lightCoords,
      final int overlayCoords,
      final boolean hasFoil,
      final int outlineColor
   ) {
      this.decoratedPotRenderer
         .submit(poseStack, submitNodeCollector, lightCoords, overlayCoords, Objects.requireNonNullElse(decorations, PotDecorations.EMPTY), outlineColor);
   }

   @Override
   public void getExtents(final Consumer<Vector3fc> output) {
      this.decoratedPotRenderer.getExtents(output);
   }
}
