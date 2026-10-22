package net.minecraft.client.renderer.feature;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.commands.RenderPass;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.StagedVertexBuffer$Draw;
import net.minecraft.client.renderer.StagedVertexBuffer$ExecuteInfo;
import net.minecraft.client.renderer.feature.submit.SubmitNode;
import net.minecraft.client.renderer.oit.OitStage;
import net.minecraft.client.renderer.rendertype.PreparedRenderType;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class RenderTypeFeatureRenderer<Submit extends SubmitNode> implements FeatureRenderer<Submit> {
   @Nullable
   private RenderTypeFeatureRenderer$Group currentGroup;
   private final List<RenderTypeFeatureRenderer$Group> groups = new ArrayList<>();

   protected abstract void buildGroup(FeatureFrameContext context, List<Submit> submits);

   protected final VertexConsumer getVertexBuilder(final RenderType renderType) {
      return this.currentGroup().getVertexBuilder(renderType);
   }

   private RenderTypeFeatureRenderer$Group currentGroup() {
      return Objects.requireNonNull(this.currentGroup, "Not preparing group");
   }

   @Override
   public final void prepareGroup(final FeatureFrameContext context, final List<Submit> submits, final boolean strictlyOrdered) {
      this.currentGroup = new RenderTypeFeatureRenderer$Group(context.stagedVertexBuffer(), !strictlyOrdered);
      this.buildGroup(context, submits);
      this.groups.add(this.currentGroup);

      for (RenderTypeFeatureRenderer$Group group : this.groups) {
         for (StagedVertexBuffer$Draw draw : group.draws) {
            context.stagedVertexBuffer().requestIndexCount(draw);
         }
      }

      this.currentGroup = null;
   }

   @Override
   public void executeGroup(
      final FeatureFrameContext context,
      @Nullable final OitStage stage,
      final RenderPass renderPass,
      final int groupIndex,
      final List<Submit> submits,
      final boolean strictlyOrdered
   ) {
      RenderTypeFeatureRenderer$Group group = this.groups.get(groupIndex);

      for (int i = 0; i < group.draws.size(); i++) {
         PreparedRenderType renderType = group.drawRenderTypes.get(i);
         StagedVertexBuffer$ExecuteInfo info = context.stagedVertexBuffer().getExecuteInfo(group.draws.get(i));
         if (info != null) {
            if (stage != null) {
               renderType.drawFromBufferOit(info, stage, renderPass);
            } else {
               renderType.drawFromBuffer(info, renderPass);
            }
         }
      }
   }

   @Override
   public void finishExecute(final FeatureFrameContext context) {
      this.groups.clear();
   }
}
