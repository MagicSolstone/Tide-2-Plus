package net.minecraft.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.PlayerSkinRenderCache$RenderInfo;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public class PlayerHeadSpecialRenderer implements SpecialModelRenderer<PlayerSkinRenderCache$RenderInfo> {
   private final PlayerSkinRenderCache playerSkinRenderCache;
   private final SkullModelBase modelBase;

   private PlayerHeadSpecialRenderer(final PlayerSkinRenderCache playerSkinRenderCache, final SkullModelBase modelBase) {
      this.playerSkinRenderCache = playerSkinRenderCache;
      this.modelBase = modelBase;
   }

   public void submit(
      final PlayerSkinRenderCache$RenderInfo argument,
      final PoseStack poseStack,
      final SubmitNodeCollector submitNodeCollector,
      final int lightCoords,
      final int overlayCoords,
      final boolean hasFoil,
      final int outlineColor
   ) {
      RenderType renderType = argument != null ? argument.renderType() : PlayerSkinRenderCache.DEFAULT_PLAYER_SKIN_RENDER_TYPE;
      SkullBlockRenderer.submitSkull(0.0F, poseStack, submitNodeCollector, lightCoords, this.modelBase, renderType, outlineColor, null);
   }

   @Override
   public void getExtents(final Consumer<Vector3fc> output) {
      PoseStack poseStack = new PoseStack();
      this.modelBase.root().getExtentsForGui(poseStack, output);
   }

   public PlayerSkinRenderCache$RenderInfo extractArgument(final ItemStack stack) {
      ResolvableProfile profile = stack.get(DataComponents.PROFILE);
      return profile == null ? null : this.playerSkinRenderCache.getOrDefault(profile);
   }
}
