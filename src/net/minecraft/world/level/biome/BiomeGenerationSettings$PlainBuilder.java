package net.minecraft.world.level.biome;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.levelgen.GenerationStep$Decoration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class BiomeGenerationSettings$PlainBuilder {
   private final List<Holder<WorldCarver>> carvers = new ArrayList<>();
   private final List<List<Holder<PlacedFeature>>> features = new ArrayList<>();

   public BiomeGenerationSettings$PlainBuilder addFeature(final GenerationStep$Decoration step, final Holder<PlacedFeature> feature) {
      return this.addFeature(step.ordinal(), feature);
   }

   public BiomeGenerationSettings$PlainBuilder addFeature(final int index, final Holder<PlacedFeature> feature) {
      this.addFeatureStepsUpTo(index);
      this.features.get(index).add(feature);
      return this;
   }

   public BiomeGenerationSettings$PlainBuilder addCarver(final Holder<WorldCarver> carver) {
      this.carvers.add(carver);
      return this;
   }

   private void addFeatureStepsUpTo(final int index) {
      while (this.features.size() <= index) {
         this.features.add(Lists.newArrayList());
      }
   }

   public BiomeGenerationSettings build() {
      return new BiomeGenerationSettings(HolderSet.direct(this.carvers), this.features.stream().map(HolderSet::direct).collect(ImmutableList.toImmutableList()));
   }
}
