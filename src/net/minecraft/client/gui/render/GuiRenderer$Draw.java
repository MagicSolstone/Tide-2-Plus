package net.minecraft.client.gui.render;

import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.StagedVertexBuffer$Draw;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
record GuiRenderer$Draw(StagedVertexBuffer$Draw draw, RenderPipeline pipeline, TextureSetup textureSetup, @Nullable ScreenRectangle scissorArea) {
}
