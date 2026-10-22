package com.li64.tide.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import net.minecraft.util.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.Identifier;

import java.util.function.Function;

public abstract class TideRenderTypes {
    private static final Function<Identifier, RenderType> FULL_WHITE_RENDER_TYPE;
    private static final Function<Identifier, RenderType> FULL_WHITE_ITEM_RENDER_TYPE;
    private static final RenderStateShard.ShaderStateShard FULL_WHITE_SHADER;
    private static final RenderStateShard.ShaderStateShard FULL_WHITE_ITEM_SHADER;

    static {
        FULL_WHITE_SHADER = new RenderStateShard.ShaderStateShard(TideCoreShaders::fullWhite);
        FULL_WHITE_ITEM_SHADER = new RenderStateShard.ShaderStateShard(TideCoreShaders::fullWhiteItem);

        FULL_WHITE_RENDER_TYPE = Util.memoize(tex -> RenderType.create(
                "full_white",
                DefaultVertexFormat.POSITION_TEX_COLOR,
                VertexFormat.Mode.QUADS,
                786432,
                RenderType.CompositeState.builder()
                        .setTextureState(new RenderStateShard.TextureStateShard(tex, false, false))
                        .setShaderState(FULL_WHITE_SHADER)
                        .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                        .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                        .createCompositeState(false))
        );
        FULL_WHITE_ITEM_RENDER_TYPE = Util.memoize(tex -> RenderType.create(
                "full_white_item",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                786432,
                RenderType.CompositeState.builder()
                        .setTextureState(new RenderStateShard.TextureStateShard(tex, false, false))
                        .setShaderState(FULL_WHITE_ITEM_SHADER)
                        .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                        .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                        .createCompositeState(false))
        );
    }

    public static RenderType singleColor(Identifier location) {
        return FULL_WHITE_RENDER_TYPE.apply(location);
    }

    @SuppressWarnings("deprecation")
    public static RenderType singleColorItem() {
        return FULL_WHITE_ITEM_RENDER_TYPE.apply(TextureAtlas.LOCATION_BLOCKS);
    }
}