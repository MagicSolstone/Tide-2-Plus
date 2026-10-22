package com.li64.tide.registries.entities.renderers;

import com.li64.tide.registries.entities.models.FishModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

/** Glowing eyes: the fish's <name>_eyes.png drawn full-bright over it. */
public class GlowingEyesLayer extends EyesLayer<FishRenderState, FishModel> {
    private final RenderType eyeRenderType;

    public GlowingEyesLayer(RenderLayerParent<FishRenderState, FishModel> parent, Identifier texture) {
        super(parent);
        this.eyeRenderType = RenderTypes.beaconBeam(texture.withPath(original ->
                original.substring(0, original.indexOf(".png")) + "_eyes.png"), true);
    }

    @Override
    public @NotNull RenderType renderType() {
        return eyeRenderType;
    }
}
