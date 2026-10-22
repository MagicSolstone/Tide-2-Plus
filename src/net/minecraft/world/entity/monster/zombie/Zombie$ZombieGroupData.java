package net.minecraft.world.entity.monster.zombie;

import net.minecraft.world.entity.SpawnGroupData;

public class Zombie$ZombieGroupData implements SpawnGroupData {
   public final boolean isBaby;
   public final boolean canSpawnJockey;

   public Zombie$ZombieGroupData(final boolean baby, final boolean canSpawnJockey) {
      this.isBaby = baby;
      this.canSpawnJockey = canSpawnJockey;
   }
}
