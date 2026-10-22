package net.minecraft.world.entity;

public class AgeableMob$AgeableMobGroupData implements SpawnGroupData {
   private int groupSize;
   private final boolean shouldSpawnBaby;
   private final float babySpawnChance;

   public AgeableMob$AgeableMobGroupData(final boolean shouldSpawnBaby, final float babySpawnChance) {
      this.shouldSpawnBaby = shouldSpawnBaby;
      this.babySpawnChance = babySpawnChance;
   }

   public AgeableMob$AgeableMobGroupData(final boolean shouldSpawnBaby) {
      this(shouldSpawnBaby, 0.05F);
   }

   public AgeableMob$AgeableMobGroupData(final float babySpawnChance) {
      this(true, babySpawnChance);
   }

   public int getGroupSize() {
      return this.groupSize;
   }

   public void increaseGroupSizeByOne() {
      this.groupSize++;
   }

   public boolean isShouldSpawnBaby() {
      return this.shouldSpawnBaby;
   }

   public float getBabySpawnChance() {
      return this.babySpawnChance;
   }
}
