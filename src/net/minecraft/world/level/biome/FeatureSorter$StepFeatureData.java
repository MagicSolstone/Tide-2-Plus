package net.minecraft.world.level.biome;

import java.util.List;
import java.util.function.ToIntFunction;
import net.minecraft.util.Util;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public record FeatureSorter$StepFeatureData(List<PlacedFeature> features, ToIntFunction<PlacedFeature> indexMapping) {
   private FeatureSorter$StepFeatureData(final List<PlacedFeature> features) {
      this(features, Util.createIndexIdentityLookup(features));
   }
}
