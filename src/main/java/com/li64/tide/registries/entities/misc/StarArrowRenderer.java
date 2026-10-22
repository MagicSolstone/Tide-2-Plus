package com.li64.tide.registries.entities.misc;

import com.li64.tide.Tide;
import com.li64.tide.registries.entities.misc.fishing.StarArrowModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import net.minecraft.util.FastColor;

public class StarArrowRenderer extends EntityRenderer<StarArrow> {
    public static final Identifier STAR_ARROW_LOCATION = Tide.resource("textures/entity/projectiles/star_arrow.png");

    private final StarArrowModel model;

    public StarArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new StarArrowModel(context.bakeLayer(StarArrowModel.MODEL_LOCATION));
    }

    public @NotNull Identifier getTextureLocation(StarArrow arrow) {
        return STAR_ARROW_LOCATION;
    }

    @Override
    protected int getBlockLightLevel(StarArrow entity, BlockPos pos) {
        return 15;
    }

    @Override
    public void render(StarArrow entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.pushPose();
        poseStack.translate(0, 0.25, 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 90f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.getXRot())));
        poseStack.scale(1, -1, 1);
        poseStack.translate(0, -1.28, 0);
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));
        model.renderToBuffer(poseStack, buffer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color(255, 255, 255, 255));
        poseStack.popPose();
    }
}