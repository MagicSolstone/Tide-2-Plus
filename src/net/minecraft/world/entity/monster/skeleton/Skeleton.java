package net.minecraft.world.entity.monster.skeleton;

import com.google.common.annotations.VisibleForTesting;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData$Builder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ConversionTracker;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class Skeleton extends AbstractSkeleton {
   private static final EntityDataAccessor<Boolean> DATA_STRAY_CONVERSION_ID = SynchedEntityData.defineId(Skeleton.class, EntityDataSerializers.BOOLEAN);
   private final ConversionTracker<AbstractSkeleton> freezingTracker = new ConversionTracker<>(
      this, DATA_STRAY_CONVERSION_ID, () -> EntityTypes.STRAY, () -> 1048, () -> this.isInPowderSnow, "FreezingTime", 140, "StrayConversionTime", 300, null
   );

   public Skeleton(final EntityType<? extends Skeleton> type, final Level level) {
      super(type, level);
   }

   @Override
   protected void defineSynchedData(final SynchedEntityData$Builder entityData) {
      super.defineSynchedData(entityData);
      entityData.define(DATA_STRAY_CONVERSION_ID, false);
   }

   @Override
   public boolean isShaking() {
      return this.freezingTracker.isConverting();
   }

   @Override
   public void tick() {
      this.freezingTracker.tick();
      super.tick();
   }

   @Override
   protected void addAdditionalSaveData(final ValueOutput output) {
      super.addAdditionalSaveData(output);
      this.freezingTracker.addAdditionalSaveData(output);
   }

   @Override
   protected void readAdditionalSaveData(final ValueInput input) {
      super.readAdditionalSaveData(input);
      this.freezingTracker.readAdditionalSaveData(input);
   }

   @VisibleForTesting
   public void startFreezeConversion(final int time) {
      this.freezingTracker.startConversion(time);
   }

   @Override
   public boolean canFreeze() {
      return false;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.SKELETON_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(final DamageSource source) {
      return SoundEvents.SKELETON_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.SKELETON_DEATH;
   }

   @Override
   protected SoundEvent getStepSound() {
      return SoundEvents.SKELETON_STEP;
   }
}
