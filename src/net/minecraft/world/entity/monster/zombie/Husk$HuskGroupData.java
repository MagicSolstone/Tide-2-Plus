package net.minecraft.world.entity.monster.zombie;

public class Husk$HuskGroupData extends Zombie$ZombieGroupData {
   public boolean triedToSpawnCamelHusk = false;

   public Husk$HuskGroupData(final Zombie$ZombieGroupData groupData) {
      super(groupData.isBaby, groupData.canSpawnJockey);
   }
}
