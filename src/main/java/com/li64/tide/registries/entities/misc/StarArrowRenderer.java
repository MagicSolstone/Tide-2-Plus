package com.li64.tide.registries.entities.misc;

import com.li64.tide.Tide;
import com.li64.tide.registries.entities.misc.fishing.StarArrowModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import net.minecraft.util.ARGB;

public class StarArrowRenderer extends EntityRenderer<StarArrow, StarArrowRenderer.State> {
    public static final Identifier STAR_ARROW_LOCATION = Tide.resource("textures/entity/projectiles/star_arrow.png");

    private final StarArrowModel model;

    public StarArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new StarArrowModel(context.bakeLayer(StarArrowModel.MODEL_LOCATION));
    }

    public static class State extends net.minecraft.client.renderer.entity.state.EntityRenderState {
        public float yRot;
        public float xRot;
    }

    @Override
    public @NotNull State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(StarArrow entity, State state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.yRot = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
        state.xRot = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
    }

    @Override
    protected int getBlockLightLevel(StarArrow entity, BlockPos pos) {
        return 15;
    }

    @Override
    public void submit(State state, PoseStack poseStack, net.minecraft.client.renderer.SubmitNodeCollector collector,
                       net.minecraft.client.renderer.state.level.CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0, 0.25, 0);
        poseStack.rotateDegrees(Axis.YP, state.yRot - 90f);
        poseStack.rotateDegrees(Axis.ZP, state.xRot);
        poseStack.scale(1, -1, 1);
        poseStack.translate(0, -1.28, 0);
        collector.submitModel(this.model, state, poseStack, net.minecraft.client.renderer.rendertype.RenderTypes.entityCutout(STAR_ARROW_LOCATION),
                net.minecraft.util.LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }
}
