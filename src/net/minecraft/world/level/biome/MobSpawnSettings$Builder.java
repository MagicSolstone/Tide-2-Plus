package net.minecraft.world.level.biome;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.random.WeightedList$Builder;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class MobSpawnSettings$Builder {
   private final Map<MobCategory, WeightedList$Builder<MobSpawnSettings$SpawnerData>> spawnsByCategory = new EnumMap<>(MobCategory.class);
   private final Map<EntityType<?>, MobSpawnSettings$MobSpawnCost> mobSpawnCosts = Maps.newLinkedHashMap();

   public MobSpawnSettings$Builder addSpawn(final EntityType<?> type, final int weight, final int minCount, final int maxCount) {
      IntProvider count;
      if (minCount == maxCount) {
         count = new ConstantInt(minCount);
      } else {
         count = new UniformInt(minCount, maxCount);
      }

      this.addSpawn(type, type.getCategory(), weight, count);
      return this;
   }

   public MobSpawnSettings$Builder addSpawn(final EntityType<?> type, final int weight, final IntProvider count) {
      this.addSpawn(type, type.getCategory(), weight, count);
      return this;
   }

   @Deprecated
   public MobSpawnSettings$Builder addSpawn(final EntityType<?> type, final MobCategory category, final int weight, final IntProvider count) {
      this.forCategory(category).add(new MobSpawnSettings$SpawnerData(type, count), weight);
      return this;
   }

   public MobSpawnSettings$Builder addAllSpawns(final MobCategory category, final WeightedList<MobSpawnSettings$SpawnerData> spawns) {
      this.forCategory(category).addAll(spawns);
      return this;
   }

   public MobSpawnSettings$Builder noSpawns(final MobCategory category) {
      this.spawnsByCategory.put(category, WeightedList.builder());
      return this;
   }

   public MobSpawnSettings$Builder dontOverride(final MobCategory category) {
      this.spawnsByCategory.remove(category);
      return this;
   }

   public MobSpawnSettings$Builder addMobSpawnCost(final EntityType<?> type, final double charge, final double energyBudget) {
      this.mobSpawnCosts.put(type, new MobSpawnSettings$MobSpawnCost(energyBudget, charge));
      return this;
   }

   public MobSpawnSettings$Builder addAllCosts(final Map<EntityType<?>, MobSpawnSettings$MobSpawnCost> costs) {
      this.mobSpawnCosts.putAll(costs);
      return this;
   }

   public MobSpawnSettings build() {
      return new MobSpawnSettings(
         this.spawnsByCategory.entrySet().stream().collect(ImmutableMap.toImmutableMap(Entry::getKey, e -> ((WeightedList$Builder)e.getValue()).build())),
         ImmutableMap.copyOf(this.mobSpawnCosts)
      );
   }

   private WeightedList$Builder<MobSpawnSettings$SpawnerData> forCategory(final MobCategory category) {
      return this.spawnsByCategory.computeIfAbsent(category, var0 -> WeightedList.builder());
   }
}
