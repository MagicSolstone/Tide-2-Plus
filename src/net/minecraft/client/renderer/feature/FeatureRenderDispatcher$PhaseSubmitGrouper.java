package net.minecraft.client.renderer.feature;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.feature.phase.FeatureRenderPhase;
import net.minecraft.client.renderer.feature.phase.FeatureRenderPhase$Output;
import net.minecraft.client.renderer.feature.submit.SubmitNode;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class FeatureRenderDispatcher$PhaseSubmitGrouper implements FeatureRenderPhase$Output {
   private final FeatureRenderDispatcher$PreparedFrame frame;
   private final List<SubmitNode> allSubmits;
   private final List<FeatureRenderDispatcher$PreparedGroup<?>> phaseGroups;
   @Nullable
   private FeatureRenderDispatcher$PreparedGroup<?> lastGroup;

   public FeatureRenderDispatcher$PhaseSubmitGrouper(final FeatureRenderDispatcher$PreparedFrame frame, final FeatureRenderPhase<?> phase) {
      this.frame = frame;
      this.allSubmits = frame.allSubmits;
      this.phaseGroups = frame.groupsByPhase.computeIfAbsent(phase, var0 -> new ArrayList<>());
   }

   @Override
   public void accept(final SubmitNode submit, final boolean strictlyOrdered) {
      int index = this.allSubmits.size();
      this.allSubmits.add(submit);
      this.addOrExtendGroup(submit.featureType(), strictlyOrdered, index, index);
   }

   @Override
   public <Submit extends SubmitNode> void acceptFeatureGroup(
      final FeatureRendererType<Submit> featureType, final Collection<Submit> submits, final boolean strictlyOrdered
   ) {
      if (!submits.isEmpty()) {
         for (Submit submit : submits) {
            if (submit.featureType() != featureType) {
               throw new IllegalArgumentException(submit + " was not of feature type " + featureType);
            }
         }

         int fromInclusive = this.allSubmits.size();
         this.allSubmits.addAll(submits);
         int toInclusive = this.allSubmits.size() - 1;
         this.addOrExtendGroup(featureType, strictlyOrdered, fromInclusive, toInclusive);
      }
   }

   private <Submit extends SubmitNode> void addOrExtendGroup(
      final FeatureRendererType<Submit> featureType, final boolean strictlyOrdered, final int fromInclusive, final int toInclusive
   ) {
      if (this.lastGroup != null && this.lastGroup.featureType == featureType && this.lastGroup.strictlyOrdered == strictlyOrdered) {
         this.lastGroup.toInclusive = toInclusive;
      } else {
         List<FeatureRenderDispatcher$PreparedGroup<?>> featureGroups = this.frame.groupsByFeature.computeIfAbsent(featureType, var0 -> new ArrayList<>());
         FeatureRenderDispatcher$PreparedGroup<Submit> group = new FeatureRenderDispatcher$PreparedGroup<>(
            featureGroups.size(), featureType, strictlyOrdered, fromInclusive, toInclusive
         );
         this.phaseGroups.add(group);
         featureGroups.add(group);
         this.lastGroup = group;
      }
   }
}
