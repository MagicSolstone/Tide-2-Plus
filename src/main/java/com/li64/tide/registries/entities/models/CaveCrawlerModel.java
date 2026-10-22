package com.li64.tide.registries.entities.models;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;

public class CaveCrawlerModel extends FishModel {
    public static final ModelLayerLocation MODEL_LOCATION = createModelLocation("cave_crawler");

    private final ModelPart legFL;
    private final ModelPart legFR;
    private final ModelPart legBL;
    private final ModelPart legBR;

    public CaveCrawlerModel(EntityRendererProvider.Context context) {
        super(context, MODEL_LOCATION);
        ModelPart front = root().getChild("front");
        this.legFL = front.getChild("leg_fl");
        this.legFR = front.getChild("leg_fr");
        this.legBL = front.getChild("leg_bl");
        this.legBR = front.getChild("leg_br");
        this.addSwimAnimation("front/head", 0.4f, -0.05f);
        this.addSwimAnimation("rear", 0.4f, 0.15f);
        this.addSwimAnimation("rear/tail", 0.4f, 0.22f);
    }

    @Override
    public float swimAnimScale() {
        return 0.0f;
    }

    @Override
    public boolean flipInAir() {
        return false;
    }

    public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

        PartDefinition front = root.addOrReplaceChild("front", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.5F, -3.0F, 5.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 21.75F, -1.0F));
        front.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 12).addBox(-1.0F, -0.5F, -2.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -5.0F, 2.5F, 0.0F, 3.1416F, 0.0F));
        front.addOrReplaceChild("head", CubeListBuilder.create().texOffs(12, 12).addBox(-1.5F, -1.5F, -3.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -0.5F, -3.0F));
        front.addOrReplaceChild("leg_fl", CubeListBuilder.create().texOffs(22, 18).addBox(0.0F, 0.75F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -1.25F, -1.0F, 0.0F, 0.0F, 0.2618F));
        front.addOrReplaceChild("leg_fr", CubeListBuilder.create().texOffs(22, 22).addBox(-3.0F, 0.75F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -1.25F, -1.0F, 0.0F, 0.0F, -0.2618F));
        front.addOrReplaceChild("leg_bl", CubeListBuilder.create().texOffs(6, 23).addBox(0.0F, 0.75F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -1.25F, 3.0F, 0.0F, 0.0F, 0.2618F));
        front.addOrReplaceChild("leg_br", CubeListBuilder.create().texOffs(24, 12).addBox(-3.0F, 0.75F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -1.25F, 3.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition rear = root.addOrReplaceChild("rear", CubeListBuilder.create().texOffs(12, 18).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.25F, 4.0F));
        rear.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 21).addBox(0.0F, -2.5F, 0.0F, 0.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 2.0F));

        return LayerDefinition.create(mesh, 64, 64);
	}

    @Override
    public void setupAnim(com.li64.tide.registries.entities.renderers.FishRenderState fish) {
        super.setupAnim(fish);
        float value = Mth.cos(1.4f * fish.walkAnimationPos) * 0.32f;
        legFL.yRot = value;
        legFR.yRot = -value;
        legBL.yRot = -value;
        legBR.yRot = value;
    }
}