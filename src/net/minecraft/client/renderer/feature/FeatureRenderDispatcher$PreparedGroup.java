package net.minecraft.client.renderer.feature;

import com.mojang.renderpearl.api.commands.RenderPass;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.feature.submit.SubmitNode;
import net.minecraft.client.renderer.oit.OitStage;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class FeatureRenderDispatcher$PreparedGroup<Submit extends SubmitNode> {
   private final int featureGroupIndex;
   private final FeatureRendererType<Submit> featureType;
   private final boolean strictlyOrdered;
   private final int fromInclusive;
   private int toInclusive;

   public FeatureRenderDispatcher$PreparedGroup(
      final int featureGroupIndex, final FeatureRendererType<Submit> featureType, final boolean strictlyOrdered, final int fromInclusive, final int toInclusive
   ) {
      this.featureGroupIndex = featureGroupIndex;
      this.featureType = featureType;
      this.strictlyOrdered = strictlyOrdered;
      this.fromInclusive = fromInclusive;
      this.toInclusive = toInclusive;
   }

   public void prepare(final FeatureFrameContext context, final FeatureRendererMap featureRenderers, final List<SubmitNode> submits) {
      FeatureRenderer<Submit> featureRenderer = featureRenderers.getOrThrow(this.featureType);
      featureRenderer.prepareGroup(context, this.sliceUnchecked(submits), this.strictlyOrdered);
   }

   public void execute(
      final FeatureFrameContext context,
      @Nullable final OitStage stage,
      final RenderPass renderPass,
      final FeatureRendererMap featureRenderers,
      final List<SubmitNode> submits
   ) {
      FeatureRenderer<Submit> featureRenderer = featureRenderers.getOrThrow(this.featureType);
      featureRenderer.executeGroup(context, stage, renderPass, this.featureGroupIndex, this.sliceUnchecked(submits), this.strictlyOrdered);
   }

   private List<Submit> sliceUnchecked(final List<SubmitNode> submits) {
      return (List<Submit>)submits.subList(this.fromInclusive, this.toInclusive + 1);
   }
}
