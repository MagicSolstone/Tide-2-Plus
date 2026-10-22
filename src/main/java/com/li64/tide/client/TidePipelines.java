package com.li64.tide.client;

import com.li64.tide.Tide;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

/** Tide's own GUI drawing pipelines. */
public final class TidePipelines {
    private TidePipelines() {}

    /** Like vanilla's textured GUI pipeline, but every visible pixel takes the given colour: a silhouette. */
    public static final RenderPipeline GUI_SILHOUETTE = RenderPipeline.builder()
            .withBindGroupLayout(BindGroupLayouts.GLOBALS)
            .withBindGroupLayout(BindGroupLayouts.PROJECTION)
            .withBindGroupLayout(BindGroupLayouts.DYNAMIC_TRANSFORMS)
            .withVertexShader("core/position_tex_color")
            .withFragmentShader(Tide.resource("core/silhouette"))
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .withLocation(Tide.resource("pipeline/gui_silhouette"))
            .build();

    /** A texture drawn as a flat silhouette in the given ARGB colour. */
    public static void silhouette(GuiGraphicsExtractor graphics, Identifier texture, int x, int y, int w, int h, int color) {
        graphics.blit(GUI_SILHOUETTE, texture, x, y, 0, 0, w, h, w, h, Gui26.opaque(color));
    }

    /** An item's silhouette, from its item texture (textures/item/<id>.png): what fish items use. */
    public static void itemSilhouette(GuiGraphicsExtractor graphics, ItemStack stack, int x, int y, int color) {
        Identifier id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        silhouette(graphics, id.withPath(p -> "textures/item/" + p + ".png"), x, y, 16, 16, color);
    }

    static RenderPipeline vanillaTextured() {
        return RenderPipelines.GUI_TEXTURED;
    }
}
