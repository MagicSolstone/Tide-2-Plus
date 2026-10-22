package net.minecraft.world.level;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.entity.MobCategory;

class LocalMobCapCalculator$MobCounts {
   private final Object2IntMap<MobCategory> counts = new Object2IntOpenHashMap(MobCategory.values().length);

   private LocalMobCapCalculator$MobCounts() {
   }

   public void add(final MobCategory category) {
      this.counts.computeInt(category, (k, count) -> count == null ? 1 : count + 1);
   }

   public boolean canSpawn(final MobCategory category) {
      return this.counts.getOrDefault(category, 0) < category.getMaxInstancesPerChunk();
   }
}
