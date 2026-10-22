package net.minecraft.world.level.levelgen.feature;

import com.google.common.cache.CacheLoader;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.List;
import java.util.stream.IntStream;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;

class EndSpikeFeature$SpikeCacheLoader extends CacheLoader<Long, List<EndSpikeFeature$EndSpike>> {
   private EndSpikeFeature$SpikeCacheLoader() {
   }

   public List<EndSpikeFeature$EndSpike> load(final Long seed) {
      IntArrayList sizes = Util.toShuffledList(IntStream.range(0, 10), RandomSource.createThreadLocalInstance(seed));
      List<EndSpikeFeature$EndSpike> result = Lists.newArrayList();

      for (int i = 0; i < 10; i++) {
         int x = Mth.floor(42.0 * Math.cos(2.0 * (-Math.PI + (Math.PI / 10) * i)));
         int z = Mth.floor(42.0 * Math.sin(2.0 * (-Math.PI + (Math.PI / 10) * i)));
         int size = sizes.get(i);
         int radius = 2 + size / 3;
         int height = 76 + size * 3;
         boolean guarded = size == 1 || size == 2;
         result.add(new EndSpikeFeature$EndSpike(x, z, radius, height, guarded));
      }

      return result;
   }
}
