package net.minecraft.world.level.block.entity.trialspawner;

import net.minecraft.resources.ResourceKey;

record TrialSpawnerConfigs$Keys(ResourceKey<TrialSpawnerConfig> normal, ResourceKey<TrialSpawnerConfig> ominous) {
   public static TrialSpawnerConfigs$Keys of(final String id) {
      return new TrialSpawnerConfigs$Keys(TrialSpawnerConfigs.registryKey(id + "/normal"), TrialSpawnerConfigs.registryKey(id + "/ominous"));
   }
}
