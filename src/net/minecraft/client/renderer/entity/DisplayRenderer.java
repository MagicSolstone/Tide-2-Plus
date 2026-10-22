package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Transformation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.state.DisplayEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Display$RenderState;
import net.minecraft.world.phys.AABB;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public abstract class DisplayRenderer<T extends Display, S, ST extends DisplayEntityRenderState> extends EntityRenderer<T, ST> {
   public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
   private final EntityRenderDispatcher entityRenderDispatcher;
   protected final BlockModelResolver blockModelResolver;

   protected DisplayRenderer(final EntityRendererProvider$Context context) {
      super(context);
      this.entityRenderDispatcher = context.getEntityRenderDispatcher();
      this.blockModelResolver = context.getBlockModelResolver();
   }

   protected AABB getBoundingBoxForCulling(final T entity, final float partialTicks) {
      return entity.getInterpolatedBoundingBox(partialTicks);
   }

   protected boolean affectedByCulling(final T entity) {
      return entity.affectedByCulling();
   }

   private static int getBrightnessOverride(final Display entity) {
      Display$RenderState renderState = entity.renderState();
      return renderState != null ? renderState.brightnessOverride() : -1;
   }

   protected int getSkyLightLevel(final T entity, final BlockPos blockPos) {
      int packedBrightnessOverride = getBrightnessOverride(entity);
      return packedBrightnessOverride != -1 ? LightCoordsUtil.sky(packedBrightnessOverride) : super.getSkyLightLevel(entity, blockPos);
   }

   protected int getBlockLightLevel(final T entity, final BlockPos blockPos) {
      int packedBrightnessOverride = getBrightnessOverride(entity);
      return packedBrightnessOverride != -1 ? LightCoordsUtil.block(packedBrightnessOverride) : super.getBlockLightLevel(entity, blockPos);
   }

   protected float getShadowRadius(final ST state) {
      Display$RenderState renderState = state.renderState;
      return renderState == null ? 0.0F : renderState.shadowRadius().get(state.interpolationProgress);
   }

   protected float getShadowStrength(final ST state) {
      Display$RenderState renderState = state.renderState;
      return renderState == null ? 0.0F : renderState.shadowStrength().get(state.interpolationProgress);
   }

   public void submit(final ST state, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final CameraRenderState camera) {
      Display$RenderState renderState = state.renderState;
      if (renderState != null && state.hasSubState()) {
         float interpolationProgress = state.interpolationProgress;
         super.submit(state, poseStack, submitNodeCollector, camera);
         poseStack.pushPose();
         poseStack.rotate(this.calculateOrientation(renderState, state, new Quaternionf()));
         Transformation transformation = renderState.transformation().get(interpolationProgress);
         poseStack.mulPose(transformation);
         this.submitInner(state, poseStack, submitNodeCollector, state.lightCoords, interpolationProgress);
         poseStack.popPose();
      }
   }

   private Quaternionf calculateOrientation(final Display$RenderState renderState, final ST state, final Quaternionf output) {
      return switch (renderState.billboardConstraints()) {
         case FIXED -> output.rotationYXZ((float) (-Math.PI / 180.0) * state.entityYRot, (float) (Math.PI / 180.0) * state.entityXRot, 0.0F);
         case HORIZONTAL -> output.rotationYXZ((float) (-Math.PI / 180.0) * state.entityYRot, (float) (Math.PI / 180.0) * transformXRot(state.cameraXRot), 0.0F);
         case VERTICAL -> output.rotationYXZ((float) (-Math.PI / 180.0) * transformYRot(state.cameraYRot), (float) (Math.PI / 180.0) * state.entityXRot, 0.0F);
         case CENTER -> output.rotationYXZ(
            (float) (-Math.PI / 180.0) * transformYRot(state.cameraYRot), (float) (Math.PI / 180.0) * transformXRot(state.cameraXRot), 0.0F
         );
      };
   }

   private static float transformYRot(final float cameraYRot) {
      return cameraYRot - 180.0F;
   }

   private static float transformXRot(final float cameraXRot) {
      return -cameraXRot;
   }

   private static <T extends Display> float entityYRot(final T entity, final float partialTicks) {
      return entity.getYRot(partialTicks);
   }

   private static <T extends Display> float entityXRot(final T entity, final float partialTicks) {
      return entity.getXRot(partialTicks);
   }

   protected abstract void submitInner(
      final ST state, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final int lightCoords, final float interpolationProgress
   );

   public void extractRenderState(final T entity, final ST state, final float partialTicks) {
      super.extractRenderState(entity, state, partialTicks);
      state.renderState = entity.renderState();
      state.interpolationProgress = entity.calculateInterpolationProgress(partialTicks);
      state.entityYRot = entityYRot(entity, partialTicks);
      state.entityXRot = entityXRot(entity, partialTicks);
      Camera camera = this.entityRenderDispatcher.camera;
      state.cameraXRot = camera.xRot();
      state.cameraYRot = camera.yRot();
   }
}
