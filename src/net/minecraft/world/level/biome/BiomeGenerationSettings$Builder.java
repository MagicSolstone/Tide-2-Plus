package net.minecraft.world.level.biome;

import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep$Decoration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class BiomeGenerationSettings$Builder extends BiomeGenerationSettings$PlainBuilder {
   private final HolderGetter<PlacedFeature> placedFeatures;
   private final HolderGetter<WorldCarver> worldCarvers;

   public BiomeGenerationSettings$Builder(final HolderGetter<PlacedFeature> placedFeatures, final HolderGetter<WorldCarver> worldCarvers) {
      this.placedFeatures = placedFeatures;
      this.worldCarvers = worldCarvers;
   }

   public BiomeGenerationSettings$Builder addFeature(final GenerationStep$Decoration step, final ResourceKey<PlacedFeature> feature) {
      this.addFeature(step.ordinal(), this.placedFeatures.getOrThrow(feature));
      return this;
   }

   public BiomeGenerationSettings$Builder addCarver(final ResourceKey<WorldCarver> carver) {
      this.addCarver(this.worldCarvers.getOrThrow(carver));
      return this;
   }
}
