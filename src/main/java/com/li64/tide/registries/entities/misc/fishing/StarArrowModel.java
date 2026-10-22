package com.li64.tide.registries.entities.misc.fishing;

import com.li64.tide.Tide;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class StarArrowModel extends EntityModel<net.minecraft.client.renderer.entity.state.EntityRenderState> {
    public static final ModelLayerLocation MODEL_LOCATION = new ModelLayerLocation(Tide.resource("star_arrow"), "main");

    public StarArrowModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("1", CubeListBuilder.create().texOffs(0, -4).addBox(0.0F, -5.0F, -5.0F, 0.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 20.5F, 0.0F, 0.0F, 1.5708F, 0.0F));
        root.addOrReplaceChild("2", CubeListBuilder.create().texOffs(0, 16).addBox(-5.0F, -5.0F, 0.0F, 10.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 20.5F, 0.0F, -1.5708F, 1.5708F, -1.5708F));

        return LayerDefinition.create(mesh, 32, 32);
    }
}
