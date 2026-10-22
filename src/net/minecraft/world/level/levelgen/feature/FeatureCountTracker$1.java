package net.minecraft.world.level.levelgen.feature;

import com.google.common.cache.CacheLoader;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import org.apache.commons.lang3.mutable.MutableInt;

class FeatureCountTracker$1 extends CacheLoader<ServerLevel, FeatureCountTracker$LevelData> {
   public FeatureCountTracker$LevelData load(final ServerLevel level) {
      return new FeatureCountTracker$LevelData(Object2IntMaps.synchronize(new Object2IntOpenHashMap()), new MutableInt(0));
   }
}
