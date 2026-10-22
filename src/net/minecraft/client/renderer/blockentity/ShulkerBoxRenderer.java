package net.minecraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Transformation;
import java.util.Map;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.state.ShulkerBoxRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer$CrumblingOverlay;
import net.minecraft.client.renderer.special.SpecialModelRenderer$BakingContext;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Direction;
import net.minecraft.util.Util;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ShulkerBoxRenderer implements BlockEntityRenderer<ShulkerBoxBlockEntity, ShulkerBoxRenderState> {
   private static final Map<Direction, Transformation> TRANSFORMATIONS = Util.makeEnumMap(Direction.class, ShulkerBoxRenderer::createModelTransform);
   private final SpriteGetter sprites;
   private final ShulkerBoxRenderer$ShulkerBoxModel model;

   public ShulkerBoxRenderer(final BlockEntityRendererProvider$Context context) {
      this(context.entityModelSet(), context.sprites());
   }

   public ShulkerBoxRenderer(final SpecialModelRenderer$BakingContext context) {
      this(context.entityModelSet(), context.sprites());
   }

   public ShulkerBoxRenderer(final EntityModelSet context, final SpriteGetter sprites) {
      this.sprites = sprites;
      this.model = new ShulkerBoxRenderer$ShulkerBoxModel(context.bakeLayer(ModelLayers.SHULKER_BOX));
   }

   public ShulkerBoxRenderState createRenderState() {
      return new ShulkerBoxRenderState();
   }

   public void extractRenderState(
      final ShulkerBoxBlockEntity blockEntity,
      final ShulkerBoxRenderState state,
      final float partialTicks,
      final Vec3 cameraPosition,
      @Nullable final ModelFeatureRenderer$CrumblingOverlay breakProgress
   ) {
      BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
      state.direction = blockEntity.getBlockState().getValueOrElse(ShulkerBoxBlock.FACING, Direction.UP);
      state.color = blockEntity.getColor();
      state.progress = blockEntity.getProgress(partialTicks);
   }

   public void submit(
      final ShulkerBoxRenderState state, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final CameraRenderState camera
   ) {
      DyeColor color = state.color;
      SpriteId sprite;
      if (color == null) {
         sprite = Sheets.DEFAULT_SHULKER_TEXTURE_LOCATION;
      } else {
         sprite = Sheets.getShulkerBoxSprite(color);
      }

      this.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.direction, state.progress, state.breakProgress, sprite, 0);
   }

   private void submit(
      final PoseStack poseStack,
      final SubmitNodeCollector submitNodeCollector,
      final int lightCoords,
      final int overlayCoords,
      final Direction direction,
      final float progress,
      @Nullable final ModelFeatureRenderer$CrumblingOverlay breakProgress,
      final SpriteId sprite,
      final int outlineColor
   ) {
      poseStack.pushPose();
      poseStack.mulPose(modelTransform(direction));
      this.submit(poseStack, submitNodeCollector, lightCoords, overlayCoords, progress, breakProgress, sprite, outlineColor);
      poseStack.popPose();
   }

   public void submit(
      final PoseStack poseStack,
      final SubmitNodeCollector submitNodeCollector,
      final int lightCoords,
      final int overlayCoords,
      final float progress,
      @Nullable final ModelFeatureRenderer$CrumblingOverlay breakProgress,
      final SpriteId sprite,
      final int outlineColor
   ) {
      this.model.setupAnim(progress);
      submitNodeCollector.submitModel(this.model, progress, poseStack, lightCoords, overlayCoords, -1, sprite, this.sprites, outlineColor);
      if (breakProgress != null) {
         submitNodeCollector.order(1)
            .submitCrumblingOverlay(this.model, progress, poseStack, sprite.renderType(this.model.renderType()), lightCoords, overlayCoords, -1, breakProgress);
      }
   }

   private static Transformation createModelTransform(final Direction direction) {
      float scale = 0.9995F;
      return new Transformation(
         new Matrix4f()
            .translation(0.5F, 0.5F, 0.5F)
            .scale(0.9995F, 0.9995F, 0.9995F)
            .rotate(direction.getRotation())
            .scale(1.0F, -1.0F, -1.0F)
            .translate(0.0F, -1.0F, 0.0F)
      );
   }

   public static Transformation modelTransform(final Direction direction) {
      return TRANSFORMATIONS.get(direction);
   }

   public void getExtents(final float progress, final Consumer<Vector3fc> output) {
      PoseStack poseStack = new PoseStack();
      this.model.setupAnim(progress);
      this.model.root().getExtentsForGui(poseStack, output);
   }
}
