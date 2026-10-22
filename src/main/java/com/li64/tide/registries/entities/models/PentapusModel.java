package com.li64.tide.registries.entities.models;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.ARGB;

public class PentapusModel extends FishModel {
    public static final ModelLayerLocation MODEL_LOCATION = createModelLocation("pentapus");

    public PentapusModel(EntityRendererProvider.Context context) {
        super(context, MODEL_LOCATION);
        this.addSwimAnimation("top/tentacles/1/bone", 0.2f, 0.25f, SwimAxis.Z);
        this.addSwimAnimation("top/tentacles/2/bone", 0.2f, 0.25f, SwimAxis.Z);
        this.addSwimAnimation("top/tentacles/3/bone", 0.2f, 0.25f, SwimAxis.Z);
        this.addSwimAnimation("top/tentacles/4/bone", 0.2f, 0.25f, SwimAxis.Z);
        this.addSwimAnimation("top/tentacles/5/bone", 0.2f, 0.25f, SwimAxis.Z);
    }

    @Override
    public int brightnessOverride() {
        return 4;
    }

    @Override
    public boolean renderTranslucent() {
        return true;
    }

    @Override
    public int tint() {
        return ARGB.ARGB32.color(150, 255, 255, 255);
    }

    @Override
    public double xTiltScale() {
        return 0;
    }

    @Override
    public float swimAnimSpeed() {
        return 0.1f;
    }

    public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

        PartDefinition top = root.addOrReplaceChild("top", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -6.5F, -0.5F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(12, 0).addBox(-0.5F, -5.5F, 0.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(20, 3).addBox(-1.0F, -2.5F, 0.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 22.5F, -1.5F));

        PartDefinition tentacles = top.addOrReplaceChild("tentacles", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.5F));
        PartDefinition group1 = tentacles.addOrReplaceChild("1", CubeListBuilder.create(), PartPose.offset(0.5F, -1.5F, 1.0F));
        group1.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(10, 13).addBox(-5.5F, -2.0F, 0.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition group2 = tentacles.addOrReplaceChild("2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.5F, -1.5F, 1.0F, 0.0F, -1.2566F, 0.0F));
        group2.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 8).addBox(-5.5F, -2.0F, 0.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition group3 = tentacles.addOrReplaceChild("3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.5F, -1.5F, 1.0F, 0.0F, -2.5133F, 0.0F));
        group3.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(10, 8).addBox(-5.5F, -2.0F, 0.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition group4 = tentacles.addOrReplaceChild("4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.5F, -1.5F, 1.0F, 0.0F, 2.5133F, 0.0F));
        group4.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(20, 8).addBox(-5.5F, -2.0F, 0.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition group5 = tentacles.addOrReplaceChild("5", CubeListBuilder.create(), PartPose.offsetAndRotation(0.5F, -1.5F, 1.0F, 0.0F, 1.2566F, 0.0F));
        group5.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 13).addBox(-5.5F, -2.0F, 0.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 32, 32);
	}
}