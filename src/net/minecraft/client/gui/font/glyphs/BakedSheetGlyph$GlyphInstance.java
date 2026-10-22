package net.minecraft.client.gui.font.glyphs;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font$DisplayMode;
import net.minecraft.client.gui.font.TextRenderable$Styled;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.network.chat.Style;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
record BakedSheetGlyph$GlyphInstance(float x, float y, int color, int shadowColor, BakedSheetGlyph glyph, Style style, float boldOffset, float shadowOffset)
   implements TextRenderable$Styled {
   @Override
   public float left() {
      return this.glyph.left(this);
   }

   @Override
   public float top() {
      return this.glyph.top(this);
   }

   @Override
   public float right() {
      return this.glyph.right(this);
   }

   @Override
   public float activeRight() {
      return this.x + this.glyph.info.getAdvance(this.style.isBold());
   }

   @Override
   public float bottom() {
      return this.glyph.bottom(this);
   }

   private boolean hasShadow() {
      return this.shadowColor() != 0;
   }

   @Override
   public void render(final Matrix4fc pose, final VertexConsumer buffer, final int packedLightCoords, final boolean flat) {
      this.glyph.renderChar(this, pose, buffer, packedLightCoords, flat);
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
