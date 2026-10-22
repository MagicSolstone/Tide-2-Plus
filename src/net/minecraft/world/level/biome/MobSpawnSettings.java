package net.minecraft.world.level.biome;

import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.Util;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class MobSpawnSettings {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final float DEFAULT_CREATURE_WORLD_GEN_SPAWN_PROBABILITY = 0.1F;
   public static final WeightedList<MobSpawnSettings$SpawnerData> EMPTY_MOB_LIST = WeightedList.of();
   public static final MobSpawnSettings EMPTY = new MobSpawnSettings$Builder().build();
   public static final MobSpawnSettings NO_SPAWNS = Util.make(() -> {
      MobSpawnSettings$Builder builder = new MobSpawnSettings$Builder();

      for (MobCategory category : MobCategory.values()) {
         builder.noSpawns(category);
      }

      return builder.build();
   });
   public static final Codec<MobSpawnSettings> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.simpleMap(
                  MobCategory.CODEC,
                  WeightedList.codec(MobSpawnSettings$SpawnerData.CODEC).promotePartial(Util.prefix("Spawn data: ", LOGGER::error)),
                  StringRepresentable.keys(MobCategory.values())
               )
               .fieldOf("spawns_by_category")
               .forGetter(b -> b.spawnsByCategory),
            Codec.simpleMap(BuiltInRegistries.ENTITY_TYPE.byNameCodec(), MobSpawnSettings$MobSpawnCost.CODEC, BuiltInRegistries.ENTITY_TYPE)
               .fieldOf("spawn_costs")
               .forGetter(b -> b.mobSpawnCosts)
         )
         .apply(i, MobSpawnSettings::new)
   );
   private final Map<MobCategory, WeightedList<MobSpawnSettings$SpawnerData>> spawnsByCategory;
   private final Map<EntityType<?>, MobSpawnSettings$MobSpawnCost> mobSpawnCosts;

   private MobSpawnSettings(
      final Map<MobCategory, WeightedList<MobSpawnSettings$SpawnerData>> spawnsByCategory,
      final Map<EntityType<?>, MobSpawnSettings$MobSpawnCost> mobSpawnCosts
   ) {
      this.spawnsByCategory = ImmutableMap.copyOf(spawnsByCategory);
      this.mobSpawnCosts = ImmutableMap.copyOf(mobSpawnCosts);
   }

   public WeightedList<MobSpawnSettings$SpawnerData> getMobsToSpawn(final MobCategory category) {
      return this.spawnsByCategory.getOrDefault(category, EMPTY_MOB_LIST);
   }

   @Nullable
   public WeightedList<MobSpawnSettings$SpawnerData> getMobsInCategory(final MobCategory category) {
      return this.spawnsByCategory.get(category);
   }

   public Set<MobCategory> definedCategories() {
      return this.spawnsByCategory.keySet();
   }

   @Nullable
   public MobSpawnSettings$MobSpawnCost getMobSpawnCost(final EntityType<?> type) {
      return this.mobSpawnCosts.get(type);
   }

   public Map<EntityType<?>, MobSpawnSettings$MobSpawnCost> allSpawnCosts() {
      return this.mobSpawnCosts;
   }
}
