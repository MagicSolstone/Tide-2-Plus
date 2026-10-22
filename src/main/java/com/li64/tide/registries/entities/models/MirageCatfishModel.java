package com.li64.tide.registries.entities.models;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.ARGB;

public class MirageCatfishModel extends FishModel {
    public static final ModelLayerLocation MODEL_LOCATION = createModelLocation("mirage_catfish");

    public MirageCatfishModel(EntityRendererProvider.Context context) {
        super(context, MODEL_LOCATION);
        this.addSwimAnimation("front/head", 0.6f, -0.15f);
        this.addSwimAnimation("rear", 0.6f, 0.15f);
        this.addSwimAnimation("rear/tail", 0.6f, 0.15f);
        this.addSwimAnimation("rear/tail/fin", 0.6f, 0.15f);
    }

    @Override
    public int tint() {
        return ARGB.color(150, 255, 255, 255);
    }

    @Override
    public boolean renderTranslucent() {
        return true;
    }
}