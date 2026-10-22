package net.minecraft.world.level.block.entity.trialspawner;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

public class TrialSpawnerConfig$Builder {
   private int spawnRange = 4;
   private float totalMobs = 6.0F;
   private float simultaneousMobs = 2.0F;
   private float totalMobsAddedPerPlayer = 2.0F;
   private float simultaneousMobsAddedPerPlayer = 1.0F;
   private int ticksBetweenSpawn = 40;
   private WeightedList<SpawnData> spawnPotentialsDefinition = WeightedList.of();
   private WeightedList<ResourceKey<LootTable>> lootTablesToEject = WeightedList.<ResourceKey<LootTable>>builder()
      .add(BuiltInLootTables.SPAWNER_TRIAL_CHAMBER_CONSUMABLES)
      .add(BuiltInLootTables.SPAWNER_TRIAL_CHAMBER_KEY)
      .build();
   private ResourceKey<LootTable> itemsToDropWhenOminous = BuiltInLootTables.SPAWNER_TRIAL_ITEMS_TO_DROP_WHEN_OMINOUS;

   public TrialSpawnerConfig$Builder spawnRange(final int spawnRange) {
      this.spawnRange = spawnRange;
      return this;
   }

   public TrialSpawnerConfig$Builder totalMobs(final float totalMobs) {
      this.totalMobs = totalMobs;
      return this;
   }

   public TrialSpawnerConfig$Builder simultaneousMobs(final float simultaneousMobs) {
      this.simultaneousMobs = simultaneousMobs;
      return this;
   }

   public TrialSpawnerConfig$Builder totalMobsAddedPerPlayer(final float totalMobsAddedPerPlayer) {
      this.totalMobsAddedPerPlayer = totalMobsAddedPerPlayer;
      return this;
   }

   public TrialSpawnerConfig$Builder simultaneousMobsAddedPerPlayer(final float simultaneousMobsAddedPerPlayer) {
      this.simultaneousMobsAddedPerPlayer = simultaneousMobsAddedPerPlayer;
      return this;
   }

   public TrialSpawnerConfig$Builder ticksBetweenSpawn(final int ticksBetweenSpawn) {
      this.ticksBetweenSpawn = ticksBetweenSpawn;
      return this;
   }

   public TrialSpawnerConfig$Builder spawnPotentialsDefinition(final WeightedList<SpawnData> spawnPotentialsDefinition) {
      this.spawnPotentialsDefinition = spawnPotentialsDefinition;
      return this;
   }

   public TrialSpawnerConfig$Builder lootTablesToEject(final WeightedList<ResourceKey<LootTable>> lootTablesToEject) {
      this.lootTablesToEject = lootTablesToEject;
      return this;
   }

   public TrialSpawnerConfig$Builder itemsToDropWhenOminous(final ResourceKey<LootTable> itemsToDropWhenOminous) {
      this.itemsToDropWhenOminous = itemsToDropWhenOminous;
      return this;
   }

   public TrialSpawnerConfig build() {
      return new TrialSpawnerConfig(
         this.spawnRange,
         this.totalMobs,
         this.simultaneousMobs,
         this.totalMobsAddedPerPlayer,
         this.simultaneousMobsAddedPerPlayer,
         this.ticksBetweenSpawn,
         this.spawnPotentialsDefinition,
         this.lootTablesToEject,
         this.itemsToDropWhenOminous
      );
   }
}
