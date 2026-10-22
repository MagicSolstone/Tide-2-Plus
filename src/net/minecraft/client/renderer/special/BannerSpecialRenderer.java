package net.minecraft.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Objects;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BannerBlock$AttachmentType;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class BannerSpecialRenderer implements SpecialModelRenderer<BannerPatternLayers> {
   private final BannerRenderer bannerRenderer;
   private final DyeColor baseColor;
   private final BannerBlock$AttachmentType attachment;

   public BannerSpecialRenderer(final DyeColor baseColor, final BannerRenderer bannerRenderer, final BannerBlock$AttachmentType attachment) {
      this.bannerRenderer = bannerRenderer;
      this.baseColor = baseColor;
      this.attachment = attachment;
   }

   @Nullable
   public BannerPatternLayers extractArgument(final ItemStack stack) {
      return stack.get(DataComponents.BANNER_PATTERNS);
   }

   public void submit(
      @Nullable final BannerPatternLayers patterns,
      final PoseStack poseStack,
      final SubmitNodeCollector submitNodeCollector,
      final int lightCoords,
      final int overlayCoords,
      final boolean hasFoil,
      final int outlineColor
   ) {
      this.bannerRenderer
         .submitSpecial(
            this.attachment,
            poseStack,
            submitNodeCollector,
            lightCoords,
            overlayCoords,
            this.baseColor,
            Objects.requireNonNullElse(patterns, BannerPatternLayers.EMPTY),
            outlineColor
         );
   }

   @Override
   public void getExtents(final Consumer<Vector3fc> output) {
      this.bannerRenderer.getExtents(output);
   }
}
