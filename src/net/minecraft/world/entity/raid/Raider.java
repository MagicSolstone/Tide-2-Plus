package net.minecraft.world.entity.raid;

import java.util.function.Predicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData$Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.PathfindToRaidGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public abstract class Raider extends PatrollingMonster {
   protected static final EntityDataAccessor<Boolean> IS_CELEBRATING = SynchedEntityData.defineId(Raider.class, EntityDataSerializers.BOOLEAN);
   private static final Predicate<ItemEntity> ALLOWED_ITEMS = e -> !e.hasPickUpDelay()
      && e.isAlive()
      && ItemStack.matches(e.getItem(), Raid.getOminousBannerInstance(e.registryAccess().lookupOrThrow(Registries.BANNER_PATTERN)));
   private static final int DEFAULT_WAVE = 0;
   private static final boolean DEFAULT_CAN_JOIN_RAID = false;
   @Nullable
   protected Raid raid;
   private int wave = 0;
   private boolean canJoinRaid = false;
   private int ticksOutsideRaid;

   protected Raider(final EntityType<? extends Raider> type, final Level level) {
      super(type, level);
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector.addGoal(1, new Raider$ObtainRaidLeaderBannerGoal<>(this, this));
      this.goalSelector.addGoal(3, new PathfindToRaidGoal<>(this));
      this.goalSelector.addGoal(4, new Raider$RaiderMoveThroughVillageGoal(this, 1.05F, 1));
      this.goalSelector.addGoal(5, new Raider$RaiderCelebration(this, this));
   }

   @Override
   protected void defineSynchedData(final SynchedEntityData$Builder entityData) {
      super.defineSynchedData(entityData);
      entityData.define(IS_CELEBRATING, false);
   }

   public abstract void applyRaidBuffs(final ServerLevel level, final int wave, final boolean isCaptain);

   public boolean canJoinRaid() {
      return this.canJoinRaid;
   }

   public void setCanJoinRaid(final boolean canJoinRaid) {
      this.canJoinRaid = canJoinRaid;
   }

   @Override
   public void aiStep() {
      if (this.level() instanceof ServerLevel level && this.isAlive()) {
         Raid currentRaid = this.getCurrentRaid();
         if (this.canJoinRaid()) {
            if (currentRaid == null) {
               if (this.level().getGameTime() % 20L == 0L) {
                  Raid nearbyRaid = level.getRaidAt(this.blockPosition());
                  if (nearbyRaid != null && Raids.canJoinRaid(this)) {
                     nearbyRaid.joinRaid(level, nearbyRaid.getGroupsSpawned(), this, null, true);
                  }
               }
            } else {
               LivingEntity target = this.getTarget();
               if (target != null && (target.is(EntityTypes.PLAYER) || target.is(EntityTypes.IRON_GOLEM))) {
                  this.noActionTime = 0;
               }
            }
         }
      }

      super.aiStep();
   }

   @Override
   protected void updateNoActionTime() {
      this.noActionTime += 2;
   }

   @Override
   public void die(final DamageSource source) {
      if (this.level() instanceof ServerLevel serverLevel) {
         Entity killer = source.getEntity();
         Raid raidWhenKilled = this.getCurrentRaid();
         if (raidWhenKilled != null) {
            if (this.isPatrolLeader()) {
               raidWhenKilled.removeLeader(this.getWave());
            }

            if (killer != null && killer.is(EntityTypes.PLAYER)) {
               raidWhenKilled.addHeroOfTheVillage(killer);
            }

            raidWhenKilled.removeFromRaid(serverLevel, this, false);
         }
      }

      super.die(source);
   }

   @Override
   public boolean canJoinPatrol() {
      return !this.hasActiveRaid();
   }

   public void setCurrentRaid(@Nullable final Raid raid) {
      this.raid = raid;
   }

   @Nullable
   public Raid getCurrentRaid() {
      return this.raid;
   }

   public boolean isCaptain() {
      ItemStack banner = this.getItemBySlot(EquipmentSlot.HEAD);
      boolean hasCaptainBanner = !banner.isEmpty()
         && ItemStack.matches(banner, Raid.getOminousBannerInstance(this.registryAccess().lookupOrThrow(Registries.BANNER_PATTERN)));
      boolean patrolLeader = this.isPatrolLeader();
      return hasCaptainBanner && patrolLeader;
   }

   public boolean hasRaid() {
      return !(this.level() instanceof ServerLevel serverLevel) ? false : this.getCurrentRaid() != null || serverLevel.getRaidAt(this.blockPosition()) != null;
   }

   public boolean hasActiveRaid() {
      return this.getCurrentRaid() != null && this.getCurrentRaid().isActive();
   }

   public void setWave(final int wave) {
      this.wave = wave;
   }

   public int getWave() {
      return this.wave;
   }

   public boolean isCelebrating() {
      return this.entityData.get(IS_CELEBRATING);
   }

   public void setCelebrating(final boolean celebrating) {
      this.entityData.set(IS_CELEBRATING, celebrating);
   }

   @Override
   protected void addAdditionalSaveData(final ValueOutput output) {
      super.addAdditionalSaveData(output);
      output.putInt("Wave", this.wave);
      output.putBoolean("CanJoinRaid", this.canJoinRaid);
      if (this.raid != null && this.level() instanceof ServerLevel level) {
         level.getRaids().getId(this.raid).ifPresent(id -> output.putInt("RaidId", id));
      }
   }

   @Override
   protected void readAdditionalSaveData(final ValueInput input) {
      super.readAdditionalSaveData(input);
      this.wave = input.getIntOr("Wave", 0);
      this.canJoinRaid = input.getBooleanOr("CanJoinRaid", false);
      if (this.level() instanceof ServerLevel level) {
         input.getInt("RaidId").ifPresent(raidId -> {
            this.raid = level.getRaids().get(raidId);
            if (this.raid != null) {
               this.raid.addWaveMob(level, this.wave, this, false);
               if (this.isPatrolLeader()) {
                  this.raid.setLeader(this.wave, this);
               }
            }
         });
      }
   }

   @Override
   protected void pickUpItem(final ServerLevel level, final ItemEntity entity) {
      ItemStack itemStack = entity.getItem();
      boolean hasRaidLeader = this.hasActiveRaid() && this.getCurrentRaid().getLeader(this.getWave()) != null;
      if (this.hasActiveRaid()
         && !hasRaidLeader
         && ItemStack.matches(itemStack, Raid.getOminousBannerInstance(this.registryAccess().lookupOrThrow(Registries.BANNER_PATTERN)))) {
         EquipmentSlot slot = EquipmentSlot.HEAD;
         ItemStack current = this.getItemBySlot(slot);
         double dropChance = this.getDropChances().byEquipment(slot);
         if (!current.isEmpty() && Math.max(this.random.nextFloat() - 0.1F, 0.0F) < dropChance) {
            this.spawnAtLocation(level, current);
         }

         this.onItemPickup(entity);
         this.setItemSlot(slot, itemStack);
         this.take(entity, itemStack.getCount());
         entity.discard();
         this.getCurrentRaid().setLeader(this.getWave(), this);
         this.setPatrolLeader(true);
      } else {
         super.pickUpItem(level, entity);
      }
   }

   @Override
   public boolean removeWhenFarAway(final double distSqr) {
      return this.getCurrentRaid() == null ? super.removeWhenFarAway(distSqr) : false;
   }

   @Override
   public boolean requiresCustomPersistence() {
      return super.requiresCustomPersistence() || this.getCurrentRaid() != null;
   }

   public int getTicksOutsideRaid() {
      return this.ticksOutsideRaid;
   }

   public void setTicksOutsideRaid(final int ticksOutsideRaid) {
      this.ticksOutsideRaid = ticksOutsideRaid;
   }

   @Override
   public boolean hurtServer(final ServerLevel level, final DamageSource source, final float damage) {
      if (this.hasActiveRaid()) {
         this.getCurrentRaid().updateBossbar();
      }

      return super.hurtServer(level, source, damage);
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(
      final ServerLevelAccessor level, final DifficultyInstance difficulty, final EntitySpawnReason spawnReason, @Nullable final SpawnGroupData groupData
   ) {
      this.setCanJoinRaid(!this.is(EntityTypes.WITCH) || spawnReason != EntitySpawnReason.NATURAL);
      return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
   }

   public abstract SoundEvent getCelebrateSound();
}
