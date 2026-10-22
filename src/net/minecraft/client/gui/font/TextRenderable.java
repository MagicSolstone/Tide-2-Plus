package net.minecraft.client.gui.font;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font$DisplayMode;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public interface TextRenderable {
   void render(Matrix4fc pose, VertexConsumer buffer, int packedLightCoords, boolean flat);

   RenderType renderType(Font$DisplayMode displayMode);

   GpuTextureView textureView();

   RenderPipeline guiPipeline();

   float left();

   float top();

   float right();

   float bottom();
}
