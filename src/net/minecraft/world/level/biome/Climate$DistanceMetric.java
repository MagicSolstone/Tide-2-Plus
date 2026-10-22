package net.minecraft.world.level.biome;

import com.google.common.annotations.VisibleForTesting;

@VisibleForTesting
interface Climate$DistanceMetric<T> {
   long distance(Climate$RTree$Node<T> node, long[] target);
}
