package net.minecraft.world.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public abstract class PatrollingMonster extends Monster {
   private static final boolean DEFAULT_PATROL_LEADER = false;
   private static final boolean DEFAULT_PATROLLING = false;
   @Nullable
   private BlockPos patrolTarget;
   private boolean patrolLeader = false;
   private boolean patrolling = false;

   protected PatrollingMonster(final EntityType<? extends PatrollingMonster> type, final Level level) {
      super(type, level);
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector.addGoal(4, new PatrollingMonster$LongDistancePatrolGoal<>(this, 0.7, 0.595));
   }

   @Override
   protected void addAdditionalSaveData(final ValueOutput output) {
      super.addAdditionalSaveData(output);
      output.storeNullable("patrol_target", BlockPos.CODEC, this.patrolTarget);
      output.putBoolean("PatrolLeader", this.patrolLeader);
      output.putBoolean("Patrolling", this.patrolling);
   }

   @Override
   protected void readAdditionalSaveData(final ValueInput input) {
      super.readAdditionalSaveData(input);
      this.patrolTarget = input.<BlockPos>read("patrol_target", BlockPos.CODEC).orElse(null);
      this.patrolLeader = input.getBooleanOr("PatrolLeader", false);
      this.patrolling = input.getBooleanOr("Patrolling", false);
   }

   public boolean canBeLeader() {
      return true;
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(
      final ServerLevelAccessor level, final DifficultyInstance difficulty, final EntitySpawnReason spawnReason, @Nullable final SpawnGroupData groupData
   ) {
      if (spawnReason != EntitySpawnReason.PATROL
         && spawnReason != EntitySpawnReason.EVENT
         && spawnReason != EntitySpawnReason.STRUCTURE
         && level.getRandom().nextFloat() < 0.06F
         && this.canBeLeader()) {
         this.patrolLeader = true;
      }

      if (this.isPatrolLeader()) {
         this.setItemSlot(EquipmentSlot.HEAD, Raid.getOminousBannerInstance(this.registryAccess().lookupOrThrow(Registries.BANNER_PATTERN)));
         this.setDropChance(EquipmentSlot.HEAD, 2.0F);
      }

      if (spawnReason == EntitySpawnReason.PATROL) {
         this.patrolling = true;
      }

      return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
   }

   public static boolean checkPatrollingMonsterSpawnRules(
      final EntityType<? extends PatrollingMonster> type,
      final LevelAccessor level,
      final EntitySpawnReason spawnReason,
      final BlockPos pos,
      final RandomSource random
   ) {
      return level.getBrightness(LightLayer.BLOCK, pos) > 8 ? false : checkAnyLightMonsterSpawnRules(type, level, spawnReason, pos, random);
   }

   @Override
   public boolean removeWhenFarAway(final double distSqr) {
      return !this.patrolling || distSqr > 16384.0;
   }

   public void setPatrolTarget(final BlockPos target) {
      this.patrolTarget = target;
      this.patrolling = true;
   }

   @Nullable
   public BlockPos getPatrolTarget() {
      return this.patrolTarget;
   }

   public boolean hasPatrolTarget() {
      return this.patrolTarget != null;
   }

   public void setPatrolLeader(final boolean isLeader) {
      this.patrolLeader = isLeader;
      this.patrolling = true;
   }

   public boolean isPatrolLeader() {
      return this.patrolLeader;
   }

   public boolean canJoinPatrol() {
      return true;
   }

   public void findPatrolTarget() {
      this.patrolTarget = this.blockPosition().offset(-500 + this.random.nextInt(1000), 0, -500 + this.random.nextInt(1000));
      this.patrolling = true;
   }

   protected boolean isPatrolling() {
      return this.patrolling;
   }

   protected void setPatrolling(final boolean value) {
      this.patrolling = value;
   }
}
