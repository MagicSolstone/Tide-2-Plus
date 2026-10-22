package net.minecraft.client.renderer.feature;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexSorting;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.StagedVertexBuffer;
import net.minecraft.client.renderer.StagedVertexBuffer$Draw;
import net.minecraft.client.renderer.rendertype.PreparedRenderType;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class RenderTypeFeatureRenderer$Group {
   private final StagedVertexBuffer stagedBuffer;
   private final boolean canReorder;
   private final List<StagedVertexBuffer$Draw> draws = new ArrayList<>();
   private final List<PreparedRenderType> drawRenderTypes = new ArrayList<>();
   @Nullable
   private RenderType lastRenderType;
   @Nullable
   private StagedVertexBuffer$Draw lastDraw;

   private RenderTypeFeatureRenderer$Group(final StagedVertexBuffer stagedBuffer, final boolean canReorder) {
      this.stagedBuffer = stagedBuffer;
      this.canReorder = canReorder;
   }

   public VertexConsumer getVertexBuilder(final RenderType renderType) {
      if (this.lastDraw == null || this.lastRenderType != renderType || !renderType.canConsolidateConsecutiveGeometry()) {
         this.lastDraw = this.getOrAddDraw(renderType);
         this.lastRenderType = renderType;
      }

      return this.stagedBuffer.getVertexBuilder(this.lastDraw);
   }

   private StagedVertexBuffer$Draw getOrAddDraw(final RenderType renderType) {
      PreparedRenderType preparedRenderType = renderType.prepare();
      int existingIndex = this.canReorder && renderType.canConsolidateConsecutiveGeometry() ? this.drawRenderTypes.indexOf(preparedRenderType) : -1;
      if (existingIndex != -1) {
         return this.draws.get(existingIndex);
      } else {
         boolean useImprovedTransparency = Minecraft.getInstance().gameRenderer.useImprovedTransparency();
         VertexSorting quadSorting = renderType.sortOnUpload() && !useImprovedTransparency ? RenderSystem.getProjectionType().vertexSorting() : null;
         StagedVertexBuffer$Draw draw = this.stagedBuffer.appendDraw(renderType.format(), renderType.primitiveTopology(), quadSorting);
         this.draws.add(draw);
         this.drawRenderTypes.add(preparedRenderType);
         return draw;
      }
   }
}
