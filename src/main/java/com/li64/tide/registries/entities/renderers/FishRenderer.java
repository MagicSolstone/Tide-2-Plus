package com.li64.tide.registries.entities.renderers;

import com.li64.tide.Tide;
import com.li64.tide.registries.entities.models.FishModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Renders every Tide fish: the model's swim animation, a body wobble, a nose tilt when climbing or diving,
 * and flopping on its side out of water. */
public class FishRenderer<M extends FishModel> extends MobRenderer<Mob, FishRenderState, FishModel> {
    private final Identifier texLocation;
    private final double xTiltScale;
    private final float swimAnimSpeed;
    private final float swimAnimScale;
    private final boolean renderTranslucent;

    public FishRenderer(String key, M model, EntityRendererProvider.Context context) {
        super(context, model, model.shadowRadius());
        this.texLocation = Tide.resource("textures/entity/fish/" + key + ".png");
        this.xTiltScale = model.xTiltScale();
        this.swimAnimSpeed = model.swimAnimSpeed();
        this.swimAnimScale = model.swimAnimScale();
        this.renderTranslucent = model.renderTranslucent();
    }

    public @NotNull Identifier getTextureLocation() {
        return this.texLocation;
    }

    @Override
    public @NotNull Identifier getTextureLocation(@NotNull FishRenderState state) {
        return this.texLocation;
    }

    @Override
    public @NotNull FishRenderState createRenderState() {
        return new FishRenderState();
    }

    @Override
    public void extractRenderState(@NotNull Mob fish, @NotNull FishRenderState state, float partialTick) {
        super.extractRenderState(fish, state, partialTick);
        state.inLiquid = fish.isInWater() || fish.isInLava();
        state.isDisplay = state.ageInTicks == 0;
        float tilt = Mth.clamp((float) (fish.getDeltaMovement().y() * 1.5 * xTiltScale), -Mth.HALF_PI, Mth.HALF_PI);
        fish.setXRot(tilt);
        state.tilt = Mth.lerp(partialTick, fish.xRotO, fish.getXRot());
    }

    @Override
    protected void setupRotations(FishRenderState fish, PoseStack poseStack, float bodyRot, float scale) {
        super.setupRotations(fish, poseStack, bodyRot, scale);
        float f = swimAnimScale * 4.3f * Mth.sin(swimAnimSpeed * fish.ageInTicks);
        poseStack.rotateDegrees(Axis.YP, (f));
        if (!fish.inLiquid && model.flipInAir()) {
            poseStack.translate(0.1f, 0.1f, -0.1f);
            poseStack.rotateDegrees(Axis.ZP, (90f));
        }
        else poseStack.rotate(Axis.XP, (fish.tilt));
    }

    @Override
    protected int getModelTint(@NotNull FishRenderState state) {
        return model.tint();
    }

    @Override
    protected @Nullable RenderType getRenderType(@NotNull FishRenderState fish, boolean isVisible, boolean forceTransparent, boolean appearsGlowing) {
        if (this.renderTranslucent && isVisible) return RenderTypes.entityTranslucent(this.getTextureLocation(fish));
        return super.getRenderType(fish, isVisible, forceTransparent, appearsGlowing);
    }

    @Override
    protected int getBlockLightLevel(@NotNull Mob fish, @NotNull BlockPos pos) {
        return Math.max(super.getBlockLightLevel(fish, pos), model.brightnessOverride());
    }
}
