package com.li64.tide.registries.entities.renderers;

import com.li64.tide.registries.entities.models.FishModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Mob;

public class GlowingEyesFishRenderer<M extends FishModel> extends FishRenderer<M> {
    public GlowingEyesFishRenderer(String key, M model, EntityRendererProvider.Context context) {
        super(key, model, context);
        this.addLayer(new GlowingEyesLayer(this, this.getTextureLocation()));
    }
}
