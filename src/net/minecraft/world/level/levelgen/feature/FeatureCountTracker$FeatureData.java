package net.minecraft.world.level.levelgen.feature;

import java.util.Optional;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

record FeatureCountTracker$FeatureData(Feature feature, Optional<PlacedFeature> topFeature) {
}
