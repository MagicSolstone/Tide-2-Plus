package com.li64.tide.registries.entities.misc.fishing;

import com.li64.tide.Tide;
import com.li64.tide.registries.TideItems;
import com.li64.tide.registries.items.FishingBobberItem;
import com.li64.tide.registries.items.FishingHookItem;
import com.li64.tide.registries.items.FishingLineItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.Map;

/** Draws a Tide hook: the hook model, the bobber on top (a model or the bobber item itself), and the
 * line back to the rod, coloured by the line item and dimmed in the dark. */
public class TideFishingHookRenderer extends EntityRenderer<TideFishingHook, TideHookRenderState> {
    private static final Identifier HOOK_TEX_LOCATION = Tide.resource("textures/entity/fishing_hook/fishing_hook.png");

    public static final Map<Item, Vec2> OFFSETS = Map.of(
            TideItems.STONE_FISHING_ROD, new Vec2(0.05f, -0.1f),
            TideItems.HONEYCOMB_FISHING_ROD, new Vec2(0.05f, -0.1f),
            TideItems.IRON_FISHING_ROD, new Vec2(0.05f, -0.02f),
            TideItems.PRISMARINE_FISHING_ROD, new Vec2(0f, -0.05f),
            TideItems.BLAZING_FISHING_ROD, new Vec2(0f, -0.1f),
            TideItems.SUNFLOWER_FISHING_ROD, new Vec2(0.05f, -0.1f)
    );

    private final TideFishingHookModel model;
    private final TideFishingHookModel bobberModel;
    private final ItemModelResolver itemModelResolver;

    public TideFishingHookRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new TideFishingHookModel(context.bakeLayer(TideFishingHookModel.MODEL_LOCATION));
        this.bobberModel = new TideFishingHookModel(context.bakeLayer(TideFishingHookModel.BOBBER_LOCATION));
        this.itemModelResolver = context.getItemModelResolver();
        this.shadowRadius = 0.1f;
    }

    @Override
    public boolean shouldRender(TideFishingHook entity, Frustum culler, double camX, double camY, double camZ, float partialTicks) {
        return super.shouldRender(entity, culler, camX, camY, camZ, partialTicks) && entity.getPlayerOwner() != null;
    }

    @Override
    protected boolean affectedByCulling(TideFishingHook entity) {
        return false;
    }

    @Override
    public @NotNull TideHookRenderState createRenderState() {
        return new TideHookRenderState();
    }

    @Override
    protected int getBlockLightLevel(TideFishingHook hook, net.minecraft.core.BlockPos pos) {
        // the glow hook (and its bobber) glow in the dark, like a glow squid
        return hook.getHook().is(com.li64.tide.registries.TideItems.GLOW_HOOK) ? 15 : super.getBlockLightLevel(hook, pos);
    }

    @Override
    public void extractRenderState(TideFishingHook hook, TideHookRenderState state, float partialTick) {
        super.extractRenderState(hook, state, partialTick);
        Player player = hook.getPlayerOwner();
        state.hasOwner = player != null;
        state.initialYaw = hook.getInitialYaw();
        state.hookTexture = allowModifiers() ? FishingHookItem.getTexture(hook.getHook()) : HOOK_TEX_LOCATION;

        ItemStack bobber = hook.getBobber();
        state.bobberIsItem = FishingBobberItem.renderItemModel(bobber);
        if (state.bobberIsItem) this.itemModelResolver.updateForNonLiving(state.bobberItem, bobber, ItemDisplayContext.FIXED, hook);
        else state.bobberTexture = FishingBobberItem.getTexture(bobber);

        if (player == null) {
            state.lineOriginOffset = Vec3.ZERO;
            return;
        }
        float swing = player.getSwingAnimation(partialTick);
        float swing2 = Mth.sin(Mth.sqrt(swing) * (float) Math.PI);
        Vec3 handPos = this.getPlayerHandPos(player, swing2, partialTick);
        Vec3 hookPos = hook.getPosition(partialTick).add(0.0, 0.25, 0.0);
        state.lineOriginOffset = handPos.subtract(hookPos);
        state.lineWidth = FishingLineItem.width(hook.getLine());
        state.lineColor = lineColor(player, FishingLineItem.getColor(hook.getLine()), FishingLineItem.glows(hook.getLine()));
    }

    @Override
    public void submit(TideHookRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (!state.hasOwner) return;
        poseStack.pushPose();
        poseStack.pushPose();

        poseStack.translate((1f / 16f) / 2f, 0, (1f / 16f) / 2f);
        poseStack.rotateDegrees(Axis.YP, 180f - state.initialYaw);
        poseStack.scale(-1.0F, -1.0F, 1.0F);

        collector.submitModel(this.model, state, poseStack, RenderTypes.entityCutout(state.hookTexture),
                state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor);

        if (state.bobberIsItem) {
            poseStack.pushPose();
            poseStack.scale(0.5f, 0.5f, 0.5f);
            poseStack.translate(0.03f, -0.22f, 0.0f);
            poseStack.rotateDegrees(Axis.ZP, 180.0f);
            state.bobberItem.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
            poseStack.popPose();
        } else if (state.bobberTexture != null) {
            collector.submitModel(this.bobberModel, state, poseStack, RenderTypes.entityCutout(state.bobberTexture),
                    state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor);
        }
        poseStack.popPose();

        float xa = (float) state.lineOriginOffset.x;
        float ya = (float) state.lineOriginOffset.y;
        float za = (float) state.lineOriginOffset.z;
        float width = Minecraft.getInstance().gameRenderer.gameRenderState().windowRenderState.appropriateLineWidth * state.lineWidth;
        int color = state.lineColor;
        collector.submitCustomGeometry(poseStack, RenderTypes.lines(), (pose, buffer) -> {
            for (int i = 0; i < 16; i++) {
                float a0 = i / 16f, a1 = (i + 1) / 16f;
                stringVertex(xa, ya, za, buffer, pose, a0, a1, width, color);
                stringVertex(xa, ya, za, buffer, pose, a1, a0, width, color);
            }
        });
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }

    /** The line's colour, dimmed by how dark it is where the player stands (unless the config says not to). */
    private static int lineColor(Player player, String colorHex, boolean glows) {
        Color color = Color.decode(colorHex);
        float brightness = Tide.CLIENT_CONFIG.general.defaultLineColor ? 0.0f : glows ? 1.0f : Mth.clamp(
                player.level().getMaxLocalRawBrightness(player.blockPosition().above()) / 15f,
                player.level().dimensionType().ambientLight(), 1f);
        return ARGB.color(255, (int) (color.getRed() * brightness), (int) (color.getGreen() * brightness), (int) (color.getBlue() * brightness));
    }

    private Vec3 getPlayerHandPos(Player player, float anim, float partialTick) {
        int i = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof FishingRodItem)) i = -i;
        Vec2 stringOffset = OFFSETS.getOrDefault(stack.getItem(), new Vec2(0f, 0f));

        if (!Tide.PLATFORM.isModLoaded("firstperson")
                && this.entityRenderDispatcher.options.getCameraType().isFirstPerson()
                && player == Minecraft.getInstance().player) {
            float fovOption = this.entityRenderDispatcher.options.fov().get().intValue();
            double d4 = 960.0 / fovOption;
            Vec3 vec3 = this.entityRenderDispatcher.camera
                    .getNearPlane(fovOption)
                    .getPointOnPlane(i * (0.525F + stringOffset.x), -0.1F + stringOffset.y)
                    .scale(d4)
                    .yRot(anim * 0.5F)
                    .xRot(-anim * 0.7F);
            return player.getEyePosition(partialTick).add(vec3);
        }
        float f = Mth.lerp(partialTick, player.yBodyRotO, player.yBodyRot) * (float) (Math.PI / 180.0);
        double d0 = Mth.sin(f);
        double d1 = Mth.cos(f);
        float f1 = player.getScale();
        double d2 = (double) i * 0.35 * (double) f1;
        double d3 = 0.8 * (double) f1;
        float f2 = player.isCrouching() ? -0.1875F : 0.0F;
        return player.getEyePosition(partialTick).add(-d1 * d2 - d0 * d3, (double) f2 - 0.45 * (double) f1, -d0 * d2 + d1 * d3);
    }

    private static void stringVertex(float xa, float ya, float za, VertexConsumer buffer, PoseStack.Pose pose,
                                     float aa, float nexta, float width, int color) {
        float x = xa * aa;
        float y = ya * (aa * aa + aa) * 0.5F + 0.25F;
        float z = za * aa;
        float nx = xa * nexta - x;
        float ny = ya * (nexta * nexta + nexta) * 0.5F + 0.25F - y;
        float nz = za * nexta - z;
        float length = Mth.sqrt(nx * nx + ny * ny + nz * nz);
        nx /= length;
        ny /= length;
        nz /= length;
        buffer.addVertex(pose, x, y, z).setColor(color).setNormal(pose, nx, ny, nz).setLineWidth(width);
    }

    /** Override this to disable bobber, hook, and line modifiers */
    protected boolean allowModifiers() { return true; }
}
