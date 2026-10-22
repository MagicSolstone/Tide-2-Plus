package net.minecraft.world.level.biome;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList.Builder;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.Graph;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.apache.commons.lang3.mutable.MutableInt;

public class FeatureSorter {
   public static <T> List<FeatureSorter$StepFeatureData> buildFeaturesPerStep(
      final List<T> featureSources, final Function<T, List<HolderSet<PlacedFeature>>> featureGetter, final boolean tryReducingError
   ) {
      Reference2IntMap<PlacedFeature> featureIndex = new Reference2IntOpenHashMap();
      MutableInt nextFeatureIndex = new MutableInt(0);
      Comparator<FeatureSorter$1FeatureData> featureDataComparator = Comparator.comparingInt(FeatureSorter$1FeatureData::step)
         .thenComparingInt(FeatureSorter$1FeatureData::featureIndex);
      Map<FeatureSorter$1FeatureData, Set<FeatureSorter$1FeatureData>> edges = new TreeMap<>(featureDataComparator);
      int maxStep = 0;

      for (T featureSource : featureSources) {
         List<FeatureSorter$1FeatureData> featureList = Lists.newArrayList();
         List<HolderSet<PlacedFeature>> featuresForStep = featureGetter.apply(featureSource);
         maxStep = Math.max(maxStep, featuresForStep.size());

         for (int i = 0; i < featuresForStep.size(); i++) {
            for (Holder<PlacedFeature> featureSupplier : featuresForStep.get(i)) {
               PlacedFeature feature = featureSupplier.value();
               featureList.add(new FeatureSorter$1FeatureData(featureIndex.computeIfAbsent(feature, f -> nextFeatureIndex.getAndIncrement()), i, feature));
            }
         }

         for (int i = 0; i < featureList.size(); i++) {
            Set<FeatureSorter$1FeatureData> data = edges.computeIfAbsent(featureList.get(i), k -> new TreeSet<>(featureDataComparator));
            if (i < featureList.size() - 1) {
               data.add(featureList.get(i + 1));
            }
         }
      }

      Set<FeatureSorter$1FeatureData> discovered = new TreeSet<>(featureDataComparator);
      Set<FeatureSorter$1FeatureData> currentlyVisiting = new TreeSet<>(featureDataComparator);
      List<FeatureSorter$1FeatureData> sortedFeatures = Lists.newArrayList();

      for (FeatureSorter$1FeatureData feature : edges.keySet()) {
         if (!currentlyVisiting.isEmpty()) {
            throw new IllegalStateException("You somehow broke the universe; DFS bork (iteration finished with non-empty in-progress vertex set");
         }

         if (!discovered.contains(feature) && Graph.depthFirstSearch(edges, discovered, currentlyVisiting, sortedFeatures::add, feature)) {
            if (!tryReducingError) {
               throw new IllegalStateException("Feature order cycle found");
            }

            List<T> reducedSources = new ArrayList<>(featureSources);

            int lastSize;
            do {
               lastSize = reducedSources.size();
               ListIterator<T> iterator = reducedSources.listIterator();

               while (iterator.hasNext()) {
                  T source = iterator.next();
                  iterator.remove();

                  try {
                     buildFeaturesPerStep(reducedSources, featureGetter, false);
                  } catch (IllegalStateException var18) {
                     continue;
                  }

                  iterator.add(source);
               }
            } while (lastSize != reducedSources.size());

            throw new IllegalStateException("Feature order cycle found, involved sources: " + reducedSources);
         }
      }

      Collections.reverse(sortedFeatures);
      Builder<FeatureSorter$StepFeatureData> features = ImmutableList.builder();

      for (int step = 0; step < maxStep; step++) {
         int finalStep = step;
         List<PlacedFeature> featuresInStep = sortedFeatures.stream()
            .filter(p -> p.step() == finalStep)
            .map(FeatureSorter$1FeatureData::feature)
            .collect(Collectors.toList());
         features.add(new FeatureSorter$StepFeatureData(featuresInStep));
      }

      return features.build();
   }
}
