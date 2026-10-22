package net.minecraft.world.entity.animal.fish;

import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData$Builder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.random.WeightedList$Builder;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Bucketable;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class Salmon extends AbstractSchoolingFish {
   private static final String TAG_TYPE = "type";
   private static final EntityDataAccessor<Integer> DATA_TYPE = SynchedEntityData.defineId(Salmon.class, EntityDataSerializers.INT);

   public Salmon(final EntityType<? extends Salmon> type, final Level level) {
      super(type, level);
      this.refreshDimensions();
   }

   @Override
   public int getMaxSchoolSize() {
      return 5;
   }

   @Override
   public ItemStack getBucketItemStack() {
      return new ItemStack(Items.SALMON_BUCKET);
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.SALMON_AMBIENT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.SALMON_DEATH;
   }

   @Override
   protected SoundEvent getHurtSound(final DamageSource source) {
      return SoundEvents.SALMON_HURT;
   }

   @Override
   protected SoundEvent getFlopSound() {
      return SoundEvents.SALMON_FLOP;
   }

   @Override
   protected void defineSynchedData(final SynchedEntityData$Builder entityData) {
      super.defineSynchedData(entityData);
      entityData.define(DATA_TYPE, Salmon$Variant.DEFAULT.id());
   }

   @Override
   public void onSyncedDataUpdated(final EntityDataAccessor<?> accessor) {
      super.onSyncedDataUpdated(accessor);
      if (DATA_TYPE.equals(accessor)) {
         this.refreshDimensions();
      }
   }

   @Override
   protected void addAdditionalSaveData(final ValueOutput output) {
      super.addAdditionalSaveData(output);
      output.store("type", Salmon$Variant.CODEC, this.getVariant());
   }

   @Override
   protected void readAdditionalSaveData(final ValueInput input) {
      super.readAdditionalSaveData(input);
      this.setVariant(input.<Salmon$Variant>read("type", Salmon$Variant.CODEC).orElse(Salmon$Variant.DEFAULT));
   }

   @Override
   public void saveToBucketTag(final ItemStack bucket) {
      Bucketable.saveDefaultDataToBucketTag(this, bucket);
      bucket.copyFrom(DataComponents.SALMON_SIZE, this);
   }

   private void setVariant(final Salmon$Variant variant) {
      this.entityData.set(DATA_TYPE, variant.id);
   }

   public Salmon$Variant getVariant() {
      return Salmon$Variant.BY_ID.apply(this.entityData.get(DATA_TYPE));
   }

   @Nullable
   @Override
   public <T> T get(final DataComponentType<? extends T> type) {
      return type == DataComponents.SALMON_SIZE ? castComponentValue((DataComponentType<T>)type, this.getVariant()) : super.get(type);
   }

   @Override
   protected void applyImplicitComponents(final DataComponentGetter components) {
      this.applyImplicitComponentIfPresent(components, DataComponents.SALMON_SIZE);
      super.applyImplicitComponents(components);
   }

   @Override
   protected <T> boolean applyImplicitComponent(final DataComponentType<T> type, final T value) {
      if (type == DataComponents.SALMON_SIZE) {
         this.setVariant(castComponentValue(DataComponents.SALMON_SIZE, value));
         return true;
      } else {
         return super.applyImplicitComponent(type, value);
      }
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(
      final ServerLevelAccessor level, final DifficultyInstance difficulty, final EntitySpawnReason spawnReason, @Nullable final SpawnGroupData groupData
   ) {
      WeightedList$Builder<Salmon$Variant> builder = WeightedList.builder();
      builder.add(Salmon$Variant.SMALL, 30);
      builder.add(Salmon$Variant.MEDIUM, 50);
      builder.add(Salmon$Variant.LARGE, 15);
      builder.build().getRandom(this.random).ifPresent(this::setVariant);
      return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
   }

   public float getSalmonScale() {
      return this.getVariant().boundingBoxScale;
   }

   @Override
   protected EntityDimensions getDefaultDimensions(final Pose pose) {
      return super.getDefaultDimensions(pose).scale(this.getSalmonScale());
   }
}
