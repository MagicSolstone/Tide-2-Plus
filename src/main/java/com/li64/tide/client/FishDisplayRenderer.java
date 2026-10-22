package com.li64.tide.client;

import com.li64.tide.compat.CompatHelper;
import com.li64.tide.data.fishing.DisplayData;
import com.li64.tide.registries.blocks.FishDisplayBlock;
import com.li64.tide.registries.blocks.entities.FishDisplayBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Draws the fish mounted on a fish display: the fish's own entity model, posed by its display data, sized by its length. */
public class FishDisplayRenderer implements BlockEntityRenderer<FishDisplayBlockEntity, FishDisplayRenderer.State> {
    private final EntityRenderDispatcher entityRenderer;

    public FishDisplayRenderer(BlockEntityRendererProvider.Context context) {
        this.entityRenderer = context.entityRenderer();
    }

    public static class State extends BlockEntityRenderState {
        public @Nullable EntityRenderState fish;
        public Direction facing = Direction.NORTH;
        public float x, y, z, roll, pitch, yaw, scale = 1f;
    }

    @Override
    public @NotNull State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(@NotNull FishDisplayBlockEntity display, @NotNull State state, float partialTick,
                                   @NotNull Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumbling) {
        BlockEntityRenderer.super.extractRenderState(display, state, partialTick, cameraPos, crumbling);
        state.fish = null;
        if (display.isEmpty()) return;
        DisplayData displayData = display.getDisplayData();
        EntityType<?> entityType = displayData.entityType();
        if (entityType == null || Minecraft.getInstance().level == null) return;

        Entity entity = display.getRenderedEntity();
        if (entity == null || entity.getType() != entityType) {
            entity = entityType.create(Minecraft.getInstance().level, EntitySpawnReason.LOAD);
            if (entity != null) {
                // never added to the world, so give it the placeholder id vanilla's spawner display uses
                entity.setId(-1);
                if (displayData.nbt().isPresent()) entity.load(TagValueInput.create(ProblemReporter.DISCARDING,
                        Minecraft.getInstance().level.registryAccess(), displayData.nbt().get()));
                if (CompatHelper.isHybridAquaticLoaded()) CompatHelper.hybridAquaticApplyVariant(entity, display.getDisplayStack());
                entity.xRotO = entity.getXRot();
                entity.yRotO = entity.getYRot();
            }
            display.setRenderedEntity(entity);
        }
        if (entity == null) return;

        state.facing = display.getBlockState().getValue(FishDisplayBlock.FACING);
        state.x = displayData.x();
        state.y = displayData.y();
        state.z = displayData.z();
        state.roll = displayData.roll();
        state.pitch = displayData.pitch();
        state.yaw = displayData.yaw();
        state.scale = Mth.sqrt((float) (display.getFishLength() / display.getBaseLength()));
        // a frozen moment: no animation time, so fish hold their display pose
        state.fish = this.entityRenderer.extractEntity(entity, 0f);
        state.fish.lightCoords = state.lightCoords;
    }

    @Override
    public void submit(@NotNull State state, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState camera) {
        if (state.fish == null) return;
        poseStack.pushPose();
        poseStack.translate(0.5f, 0.5f, 0.5f); // center rotation
        poseStack.rotateDegrees(Axis.YP, -state.facing.toYRot() + 90); // rotate based on facing direction
        poseStack.translate(0.5f, 0.0f, 0.0f); // move towards display

        poseStack.translate(-state.z, state.y, state.x);
        poseStack.rotateDegrees(Axis.ZP, state.roll + 90f);
        poseStack.rotateDegrees(Axis.XP, state.pitch);
        poseStack.rotateDegrees(Axis.YP, state.yaw);
        poseStack.scale(state.scale, state.scale, state.scale);

        this.entityRenderer.submit(state.fish, camera, 0.0, 0.0, 0.0, poseStack, collector);
        poseStack.popPose();
    }
}
