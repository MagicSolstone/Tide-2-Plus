package com.li64.tide.registries.entities.models;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.ARGB;

public class JellyfishModel extends FishModel {
    public static final ModelLayerLocation MODEL_LOCATION = createModelLocation("jellyfish");

    public JellyfishModel(EntityRendererProvider.Context context) {
        super(context, MODEL_LOCATION);
        this.addSwimAnimation("top/bottom/outer/1", 0.2f, 0.3f, 0f, -1.3f, SwimAxis.Y);
        this.addSwimAnimation("top/bottom/outer/2", 0.2f, 0.3f, 0f, -1.3f, SwimAxis.Y);
        this.addSwimAnimation("top/bottom/outer/3", 0.2f, 0.3f, 0f, 0.26f, SwimAxis.Z);
        this.addSwimAnimation("top/bottom/outer/4", 0.2f, -0.3f, 0f, -0.26f, SwimAxis.Z);
    }

    @Override
    public int brightnessOverride() {
        return 15;
    }

    @Override
    public boolean renderTranslucent() {
        return true;
    }

    @Override
    public int tint() {
        return ARGB.color(150, 255, 255, 255);
    }

    @Override
    public double xTiltScale() {
        return 0;
    }

    @Override
    public float swimAnimScale() {
        return 0;
    }

    public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

        PartDefinition top = root.addOrReplaceChild("top", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -8.5F, -1.5F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(24, 0).addBox(-1.5F, -7.5F, -0.5F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 22.5F, -1.5F));

        PartDefinition bottom = top.addOrReplaceChild("bottom", CubeListBuilder.create(), PartPose.offset(0.5F, -3.5F, 0.0F));

        PartDefinition outer = bottom.addOrReplaceChild("outer", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        outer.addOrReplaceChild("1", CubeListBuilder.create().texOffs(12, 22).addBox(0.0F, 0.0F, -3.0F, 0.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.5F, -1.5708F, -1.309F, 1.5708F));
        outer.addOrReplaceChild("2", CubeListBuilder.create().texOffs(12, 11).addBox(0.0F, 0.0F, -3.0F, 0.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.5F, 1.5708F, -1.309F, -1.5708F));
        outer.addOrReplaceChild("3", CubeListBuilder.create().texOffs(0, 22).addBox(0.0F, 0.0F, -3.0F, 0.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, 1.5F, 0.0F, 0.0F, 0.2618F));
        outer.addOrReplaceChild("4", CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, 0.0F, -3.0F, 0.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 1.5F, 0.0F, 0.0F, -0.2618F));

        PartDefinition inner = bottom.addOrReplaceChild("inner", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        inner.addOrReplaceChild("1", CubeListBuilder.create().texOffs(28, 7).addBox(0.0F, 0.0F, -1.0F, 0.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, -1.5708F, -1.3963F, 1.5708F));
        inner.addOrReplaceChild("2", CubeListBuilder.create().texOffs(24, 21).addBox(0.0F, 0.0F, -1.0F, 0.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 2.5F, 1.5708F, -1.3963F, -1.5708F));
        inner.addOrReplaceChild("3", CubeListBuilder.create().texOffs(24, 14).addBox(0.0F, 0.0F, -1.0F, 0.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 1.5F, 0.0F, 0.0F, 0.1745F));
        inner.addOrReplaceChild("4", CubeListBuilder.create().texOffs(24, 7).addBox(0.0F, 0.0F, -1.0F, 0.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 1.5F, 0.0F, 0.0F, -0.1745F));

        return LayerDefinition.create(mesh, 64, 64);
	}
}