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
        Identifier tex = id.withPath(p -> "textures/item/" + p + ".png");
        // an animated texture (the rods' swaying line) is a strip of frames: use just the first one
        int texHeight = TEXTURE_HEIGHTS.computeIfAbsent(tex, TidePipelines::textureHeight);
        graphics.blit(GUI_SILHOUETTE, tex, x, y, 0, 0, 16, 16, 16, texHeight, Gui26.opaque(color));
    }

    private static final java.util.Map<Identifier, Integer> TEXTURE_HEIGHTS = new java.util.concurrent.ConcurrentHashMap<>();

    /** A 16-wide texture's height, read from its PNG header (16 for a still texture). */
    private static int textureHeight(Identifier tex) {
        try (var in = net.minecraft.client.Minecraft.getInstance().getResourceManager().open(tex)) {
            byte[] head = in.readNBytes(24);
            int w = ((head[16] & 0xFF) << 24) | ((head[17] & 0xFF) << 16) | ((head[18] & 0xFF) << 8) | (head[19] & 0xFF);
            int h = ((head[20] & 0xFF) << 24) | ((head[21] & 0xFF) << 16) | ((head[22] & 0xFF) << 8) | (head[23] & 0xFF);
            return w > 0 ? h * 16 / w : 16;
        } catch (Exception e) {
            return 16;
        }
    }

    static RenderPipeline vanillaTextured() {
        return RenderPipelines.GUI_TEXTURED;
    }
}
