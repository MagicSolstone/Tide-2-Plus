package com.li64.tide.registries.entities.models;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class WindbassModel extends FishModel {
    public static final ModelLayerLocation MODEL_LOCATION = createModelLocation("windbass");

    public WindbassModel(EntityRendererProvider.Context context) {
        super(context, MODEL_LOCATION);
        this.addSwimAnimation("front/head", 0.6f, -0.15f);
        this.addSwimAnimation("rear", 0.6f, 0.25f);
        this.addSwimAnimation("rear/tail", 0.6f, 0.25f);
    }

    public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

        PartDefinition front = root.addOrReplaceChild("front", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -4.0F, -2.0F, 4.0F, 5.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(22, 13).addBox(0.5F, 0.5F, 4.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(0.5F, -5.5F, 4.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 21.0F, -2.0F));

        front.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, -0.5F, -3.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -6.5F, 1.0F, 0.0F, 3.1416F, 0.0F));
        front.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(6, 22).mirror().addBox(0.231F, 0.0957F, 0.0F, 0.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5F, 1.0F, -0.5F, 0.3655F, 0.147F, -0.3655F));
        front.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(6, 22).addBox(-0.231F, 0.0957F, 0.0F, 0.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 1.0F, -0.5F, 0.3655F, -0.147F, 0.3655F));
        front.addOrReplaceChild("head", CubeListBuilder.create().texOffs(10, 13).addBox(-1.5F, -2.0F, -2.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, -3.0F));
        front.addOrReplaceChild("fin_l", CubeListBuilder.create().texOffs(16, 23).addBox(0.0F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -0.5F, -1.0F, 0.0F, -1.1781F, 0.0F));
        front.addOrReplaceChild("fin_r", CubeListBuilder.create().texOffs(16, 23).mirror().addBox(-3.0F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.5F, -0.5F, -1.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition rear = root.addOrReplaceChild("rear", CubeListBuilder.create().texOffs(16, 19).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 4.0F));
        rear.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(10, 19).addBox(0.0F, -2.5F, 0.0F, 0.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 2.0F));

		return LayerDefinition.create(mesh, 32, 32);
	}
}