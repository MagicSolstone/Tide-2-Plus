package net.minecraft.client.gui.font.glyphs;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font$DisplayMode;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
record BakedSheetGlyph$EffectInstance(
   BakedSheetGlyph glyph, float x0, float y0, float x1, float y1, float depth, int color, int shadowColor, float shadowOffset
) implements TextRenderable {
   @Override
   public float left() {
      return this.x0;
   }

   @Override
   public float top() {
      return this.y0;
   }

   @Override
   public float right() {
      return this.x1 + (this.hasShadow() ? this.shadowOffset : 0.0F);
   }

   @Override
   public float bottom() {
      return this.y1 + (this.hasShadow() ? this.shadowOffset : 0.0F);
   }

   private boolean hasShadow() {
      return this.shadowColor() != 0;
   }

   @Override
   public void render(final Matrix4fc pose, final VertexConsumer buffer, final int packedLightCoords, final boolean flat) {
      this.glyph.renderEffect(this, pose, buffer, packedLightCoords, false);
   }

   @Override
   public RenderType renderType(final Font$DisplayMode displayMode) {
      return this.glyph.renderTypes.select(displayMode);
   }

   @Override
   public GpuTextureView textureView() {
      return this.glyph.textureView;
   }

   @Override
   public RenderPipeline guiPipeline() {
      return this.glyph.renderTypes.guiPipeline();
   }
}
