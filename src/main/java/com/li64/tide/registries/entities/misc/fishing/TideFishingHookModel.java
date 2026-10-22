package com.li64.tide.registries.entities.misc.fishing;

import com.li64.tide.Tide;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class TideFishingHookModel extends EntityModel<TideHookRenderState> {
    public static final ModelLayerLocation MODEL_LOCATION = new ModelLayerLocation(Tide.resource("fishing_hook"), "main");
    public static final ModelLayerLocation BOBBER_LOCATION = new ModelLayerLocation(Tide.resource("fishing_hook"), "bobber");

    public TideFishingHookModel(ModelPart root) {
        super(root, RenderTypes::entityCutout);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        partDefinition.addOrReplaceChild("bobber", CubeListBuilder.create()
                .texOffs(0, 10).addBox(-1.0F, -3.0F, -1.0F, 3.0F, 3.0F, 3.0F,
                        new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        partDefinition.addOrReplaceChild("top", CubeListBuilder.create()
                .texOffs(5, 7).addBox(0.0F, -4.0F, 0.5F, 1.0F, 1.0F, 0.0F,
                        new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        partDefinition.addOrReplaceChild("top2", CubeListBuilder.create()
                .texOffs(5, 7).addBox(-0.5F, -4.0F, 0.0F, 1.0F, 1.0F, 0.0F,
                        new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.0F, 0.5F, 0.0F, -1.5708F, 0.0F));

        partDefinition.addOrReplaceChild("hook", CubeListBuilder.create()
                .texOffs(3, 7).addBox(-2.0F, 0.0F, 0.4F, 3.0F, 3.0F, 0.0F,
                        new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshDefinition, 16, 16);
    }
}
