package com.li64.tide.registries.entities.models;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.ARGB;

public class SlimySalmonModel extends FishModel {
    public static final ModelLayerLocation MODEL_LOCATION = createModelLocation("slimy_salmon");

    public SlimySalmonModel(EntityRendererProvider.Context context) {
        super(context, MODEL_LOCATION);
        this.addSwimAnimation("front/head", 0.6f, -0.15f);
        this.addSwimAnimation("rear", 0.6f, 0.18f);
        this.addSwimAnimation("rear/tail", 0.6f, 0.25f);
    }

    @Override
    public int tint() {
        return ARGB.color(190, 255, 255, 255);
    }

    @Override
    public boolean renderTranslucent() {
        return true;
    }

    public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

        PartDefinition front = root.addOrReplaceChild("front", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -2.5F, -8.0F, 3.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.5F, 2.0F));
        front.addOrReplaceChild("head", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -8.0F));
        front.addOrReplaceChild("fin_l", CubeListBuilder.create().texOffs(22, 7).addBox(0.0F, 0.0F, 0.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 1.5F, -8.0F, 0.0F, 0.0F, 0.7854F));
        front.addOrReplaceChild("fin_r", CubeListBuilder.create().texOffs(22, 9).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 1.5F, -8.0F, 0.0F, 0.0F, -0.7854F));
        front.addOrReplaceChild("fin_top", CubeListBuilder.create().texOffs(0, 22).addBox(0.0F, -1.0F, 0.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.5F, -5.0F));

        PartDefinition rear = root.addOrReplaceChild("rear", CubeListBuilder.create().texOffs(0, 13).addBox(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.5F, 2.0F));
        rear.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(14, 13).addBox(0.0F, -2.5F, 0.0F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 4.0F));

        return LayerDefinition.create(mesh, 32, 32);
	}
}