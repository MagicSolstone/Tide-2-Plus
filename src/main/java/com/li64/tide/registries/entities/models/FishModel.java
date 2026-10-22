package com.li64.tide.registries.entities.models;

import com.li64.tide.Tide;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;

/*if >=1.21*/import net.minecraft.util.FastColor;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public abstract class FishModel extends EntityModel<Mob> {
    private final ArrayList<SwimAnimConfig> swimConfigs = new ArrayList<>();

    private final ModelPart root;

    protected FishModel(EntityRendererProvider.Context context, ModelLayerLocation modelLocation) {
        this.root = context.bakeLayer(modelLocation);
    }

    protected ModelPart root() {
        return this.root;
    }

    protected void addSwimAnimation(String path, float speed, float magnitude) {
        this.addSwimAnimation(path, speed, magnitude, 0f);
    }

    protected void addSwimAnimation(String path, float speed, float magnitude, SwimAxis axis) {
        this.addSwimAnimation(path, speed, magnitude, 0f, 0f, axis);
    }

    protected void addSwimAnimation(String path, float speed, float magnitude, float offset) {
        this.addSwimAnimation(path, speed, magnitude, offset, 0f, SwimAxis.Y);
    }

    protected void addSwimAnimation(String path, float speed, float magnitude, float offset, SwimAxis axis) {
        this.addSwimAnimation(path, speed, magnitude, offset, 0f, axis);
    }

    protected void addSwimAnimation(String path, float speed, float magnitude, float offset, float origin, SwimAxis axis) {
        String[] parts = path.split("/");
        ModelPart current = this.root();
        if (!path.isEmpty()) for (String part : parts) current = current.getChild(part);
        this.addSwimAnimation(current, speed, magnitude, offset, origin, axis);
    }

    protected void addSwimAnimation(ModelPart part, float speed, float magnitude, float offset, float origin, SwimAxis axis) {
        this.swimConfigs.add(new SwimAnimConfig(part, speed, magnitude, offset, origin, axis));
    }

    protected static ModelLayerLocation createModelLocation(String name) {
        return new ModelLayerLocation(Tide.resource(name), "main");
    }

    @Override
    public void setupAnim(Mob fish, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float multiplier = (fish.isInWaterOrBubble() || fish.isInLava()) ? 1.0F : 1.5F;
        this.swimConfigs.forEach(config -> config.animate(ageInTicks, multiplier, ageInTicks == 0));
    }

    public double xTiltScale() {
        return 1.0;
    }

    public float swimAnimSpeed() {
        return 0.6f;
    }

    public float swimAnimScale() {
        return 1.0f;
    }

    public float shadowRadius() {
        return 0.3f;
    }

    public int tint() {
        return -1;
    }

    public boolean renderTranslucent() {
        return false;
    }

    public int brightnessOverride() {
        return 0;
    }

    public boolean flipInAir() {
        return true;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.root().render(poseStack, buffer, packedLight, packedOverlay, FastColor.ARGB32.multiply(color, this.tint()));
    }

    private record SwimAnimConfig(ModelPart modelPart, float speed, float magnitude, float offset, float origin, SwimAxis axis) {
        public void animate(float clock, float multiplier, boolean isDisplay) {
            this.axis.apply(modelPart, -multiplier * magnitude * Mth.sin(speed * clock + (isDisplay ? 0 : offset)) + origin);
        }
    }

    protected enum SwimAxis {
        X((part, rot) -> part.xRot = rot),
        Y((part, rot) -> part.yRot = rot),
        Z((part, rot) -> part.zRot = rot);

        private final BiConsumer<ModelPart, Float> rotator;

        SwimAxis(BiConsumer<ModelPart, Float> rotator) {
            this.rotator = rotator;
        }

        public void apply(ModelPart part, float rot) {
            this.rotator.accept(part, rot);
        }
    }
}