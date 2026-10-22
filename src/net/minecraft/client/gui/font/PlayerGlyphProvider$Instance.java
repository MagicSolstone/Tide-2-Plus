package net.minecraft.client.gui.font;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font$DisplayMode;
import net.minecraft.client.renderer.PlayerSkinRenderCache$RenderInfo;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.network.chat.Style;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
record PlayerGlyphProvider$Instance(
   Supplier<PlayerSkinRenderCache$RenderInfo> skin, boolean hat, float x, float y, int color, int shadowColor, float shadowOffset, Style style
) implements PlainTextRenderable {
   @Override
   public void renderSprite(
      final Matrix4fc pose, final VertexConsumer buffer, final int packedLightCoords, final float offsetX, final float offsetY, final float z, final int color
   ) {
      float x0 = offsetX + this.left();
      float x1 = offsetX + this.right();
      float y0 = offsetY + this.top();
      float y1 = offsetY + this.bottom();
      renderQuad(pose, buffer, packedLightCoords, x0, x1, y0, y1, z, color, 8.0F, 8.0F, 8, 8, 64, 64);
      if (this.hat) {
         renderQuad(pose, buffer, packedLightCoords, x0, x1, y0, y1, z, color, 40.0F, 8.0F, 8, 8, 64, 64);
      }
   }

   private static void renderQuad(
      final Matrix4fc pose,
      final VertexConsumer buffer,
      final int packedLightCoords,
      final float x0,
      final float x1,
      final float y0,
      final float y1,
      final float z,
      final int color,
      final float u,
      final float v,
      final int srcWidth,
      final int srcHeight,
      final int textureWidth,
      final int textureHeight
   ) {
      float u0 = (u + 0.0F) / textureWidth;
      float u1 = (u + srcWidth) / textureWidth;
      float v0 = (v + 0.0F) / textureHeight;
      float v1 = (v + srcHeight) / textureHeight;
      buffer.addVertex(pose, x0, y0, z).setUv(u0, v0).setColor(color).setLight(packedLightCoords);
      buffer.addVertex(pose, x0, y1, z).setUv(u0, v1).setColor(color).setLight(packedLightCoords);
      buffer.addVertex(pose, x1, y1, z).setUv(u1, v1).setColor(color).setLight(packedLightCoords);
      buffer.addVertex(pose, x1, y0, z).setUv(u1, v0).setColor(color).setLight(packedLightCoords);
   }

   @Override
   public RenderType renderType(final Font$DisplayMode displayMode) {
      return this.skin.get().glyphRenderTypes().select(displayMode);
   }

   @Override
   public RenderPipeline guiPipeline() {
      return this.skin.get().glyphRenderTypes().guiPipeline();
   }

   @Override
   public GpuTextureView textureView() {
      return this.skin.get().textureView();
   }
}
