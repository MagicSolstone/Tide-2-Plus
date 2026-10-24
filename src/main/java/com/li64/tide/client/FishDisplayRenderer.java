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
                // never added to the world: an id of its own (below vanilla's -1 placeholder), so animation packs
                // that remember things per entity don't mix two displays up
                entity.setId(-1000 - (display.getBlockPos().hashCode() & 0x3FFFFFFF));
                if (displayData.nbt().isPresent()) entity.load(TagValueInput.create(ProblemReporter.DISCARDING,
                        Minecraft.getInstance().level.registryAccess(), displayData.nbt().get()));
                if (CompatHelper.isHybridAquaticLoaded()) CompatHelper.hybridAquaticApplyVariant(entity, display.getDisplayStack());
                if (entity instanceof net.minecraft.world.entity.animal.fish.TropicalFish) tropicalPattern(entity, display.getDisplayStack());
                // Animation resource packs (Fresh Animations) wiggle a fish that's out of water, turn it on its belly and
                // make a tropical fish's pattern blink. So the fish counts as in water (the pack holds it still in a
                // swimming pose), while the renderer is told it's out (so it lies on its side, as Tide's displays expect).
                if (swimsOnDisplay(entity, display.getDisplayStack())) ((com.li64.tide.mixin.EntityWaterAccessor) entity).tide$setWasTouchingWater(true);
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
        // a test hook: custom_data {tide_display:{roll:..,pitch:..,yaw:..}} on the fish turns it further (CI compares turns)
        var custom = display.getDisplayStack().get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
        if (custom != null) {
            var t = custom.copyTag().getCompoundOrEmpty("tide_display");
            state.roll += t.getFloatOr("roll", 0f);
            state.pitch += t.getFloatOr("pitch", 0f);
            state.yaw += t.getFloatOr("yaw", 0f);
        }
        state.scale = displayScale(display);
        // a frozen moment: no animation time, so fish hold their display pose
        state.fish = this.entityRenderer.extractEntity(entity, 0f);
        if (state.fish instanceof net.minecraft.client.renderer.entity.state.LivingEntityRenderState living) living.isInWater = false;
        state.fish.lightCoords = state.lightCoords;
    }

    /**
     * A caught tropical fish item doesn't say what it looked like (only a bucket does), so on the wall it was the plain
     * white default, which reads as a silverfish. Use the pattern the item has if it came from a bucket; otherwise one of
     * the game's named varieties, picked from the fish's weight and length so the same fish always looks the same.
     */
    private static boolean swimsOnDisplay(Entity entity, net.minecraft.world.item.ItemStack stack) {
        var custom = stack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);       // CI compares both ways
        if (custom != null && custom.copyTag().getCompoundOrEmpty("tide_display").contains("swim"))
            return custom.copyTag().getCompoundOrEmpty("tide_display").getBooleanOr("swim", false);
        return entity instanceof net.minecraft.world.entity.animal.fish.Cod || entity instanceof net.minecraft.world.entity.animal.fish.Salmon
                || entity instanceof net.minecraft.world.entity.animal.fish.TropicalFish;
    }

    private static void tropicalPattern(Entity entity, net.minecraft.world.item.ItemStack stack) {
        var pattern = stack.get(net.minecraft.core.component.DataComponents.TROPICAL_FISH_PATTERN);
        var base = stack.get(net.minecraft.core.component.DataComponents.TROPICAL_FISH_BASE_COLOR);
        var colour = stack.get(net.minecraft.core.component.DataComponents.TROPICAL_FISH_PATTERN_COLOR);
        if (pattern == null || base == null || colour == null) {
            var common = net.minecraft.world.entity.animal.fish.TropicalFish.COMMON_VARIANTS;
            long seed = Double.hashCode(com.li64.tide.data.item.TideItemData.FISH_WEIGHT.getOrDefault(stack, 0.0)) * 31L
                    + Double.hashCode(com.li64.tide.data.item.TideItemData.FISH_LENGTH.getOrDefault(stack, 0.0));
            var v = common.get(Math.floorMod(seed, common.size()));
            pattern = v.pattern(); base = v.baseColor(); colour = v.patternColor();
        }
        entity.setComponent(net.minecraft.core.component.DataComponents.TROPICAL_FISH_PATTERN, pattern);
        entity.setComponent(net.minecraft.core.component.DataComponents.TROPICAL_FISH_BASE_COLOR, base);
        entity.setComponent(net.minecraft.core.component.DataComponents.TROPICAL_FISH_PATTERN_COLOR, colour);
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

    /**
     * How big a fish hangs: by its weight against an average one of its kind (heavier is bigger; a Trophy, well
     * fed, looks it), the same curve the length gave before. Fish from before weights go by length.
     */
    private static float displayScale(com.li64.tide.registries.blocks.entities.FishDisplayBlockEntity display) {
        net.minecraft.world.item.ItemStack fish = display.getDisplayStack();
        double grams = com.li64.tide.data.item.TideItemData.FISH_WEIGHT.getOrDefault(fish, 0.0), base = display.getBaseLength();
        double ratio;
        if (grams > 0 && base > 0) {
            String path = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(fish.getItem()).getPath();
            double average = 0.01 * com.li64.tide.data.angler.FishWeight.build(path) * Math.pow(base, 3);
            ratio = Math.cbrt(grams / Math.max(1e-6, average));
        } else ratio = display.getFishLength() / Math.max(1e-6, base);
        return (float) Mth.clamp(Math.sqrt(ratio), 0.55, 1.7);
    }
}
