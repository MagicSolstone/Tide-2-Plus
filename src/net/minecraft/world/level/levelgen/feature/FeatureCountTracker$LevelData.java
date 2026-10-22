package net.minecraft.world.level.levelgen.feature;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.apache.commons.lang3.mutable.MutableInt;

record FeatureCountTracker$LevelData(Object2IntMap<FeatureCountTracker$FeatureData> featureData, MutableInt chunksWithFeatures) {
}
