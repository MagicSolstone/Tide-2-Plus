package net.minecraft.client.gui.font;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font$DisplayMode;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Style;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
record AtlasGlyphProvider$Instance(
   GlyphRenderTypes renderTypes,
   GpuTextureView textureView,
   TextureAtlasSprite sprite,
   float x,
   float y,
   int color,
   int shadowColor,
   float shadowOffset,
   Style style
) implements PlainTextRenderable {
   @Override
   public void renderSprite(
      final Matrix4fc pose, final VertexConsumer buffer, final int packedLightCoords, final float offsetX, final float offsetY, final float z, final int color
   ) {
      float x0 = offsetX + this.left();
      float x1 = offsetX + this.right();
      float y0 = offsetY + this.top();
      float y1 = offsetY + this.bottom();
      buffer.addVertex(pose, x0, y0, z).setUv(this.sprite.getU0(), this.sprite.getV0()).setColor(color).setLight(packedLightCoords);
      buffer.addVertex(pose, x0, y1, z).setUv(this.sprite.getU0(), this.sprite.getV1()).setColor(color).setLight(packedLightCoords);
      buffer.addVertex(pose, x1, y1, z).setUv(this.sprite.getU1(), this.sprite.getV1()).setColor(color).setLight(packedLightCoords);
      buffer.addVertex(pose, x1, y0, z).setUv(this.sprite.getU1(), this.sprite.getV0()).setColor(color).setLight(packedLightCoords);
   }

   @Override
   public RenderType renderType(final Font$DisplayMode displayMode) {
      return this.renderTypes.select(displayMode);
   }

   @Override
   public RenderPipeline guiPipeline() {
      return this.renderTypes.guiPipeline();
   }
}
