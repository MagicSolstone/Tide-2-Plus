package net.minecraft.world.entity.animal.turtle;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData$Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EntityAttachments;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions$Selector;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class Turtle extends Animal {
   private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(Turtle.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> LAYING_EGG = SynchedEntityData.defineId(Turtle.class, EntityDataSerializers.BOOLEAN);
   private static final float BABY_SCALE = 0.3F;
   private static final EntityDimensions BABY_DIMENSIONS = EntityTypes.TURTLE
      .getDimensions()
      .withAttachments(EntityAttachments.builder().attach(EntityAttachment.PASSENGER, 0.0F, EntityTypes.TURTLE.getHeight(), -0.25F))
      .scale(0.3F);
   private static final boolean DEFAULT_HAS_EGG = false;
   private int layEggCounter;
   public static final TargetingConditions$Selector BABY_ON_LAND_SELECTOR = (target, level) -> target.isBaby() && !target.isInWater();
   private BlockPos homePos = BlockPos.ZERO;
   @Nullable
   private BlockPos travelPos;
   private boolean goingHome;

   public Turtle(final EntityType<? extends Turtle> type, final Level level) {
      super(type, level);
      this.setPathfindingMalus(PathType.WATER, 0.0F);
      this.setPathfindingMalus(PathType.DOOR_IRON_CLOSED, -1.0F);
      this.setPathfindingMalus(PathType.DOOR_WOOD_CLOSED, -1.0F);
      this.setPathfindingMalus(PathType.DOOR_OPEN, -1.0F);
      this.moveControl = new Turtle$TurtleMoveControl<>(this);
   }

   public void setHomePos(final BlockPos pos) {
      this.homePos = pos;
   }

   public BlockPos getHomePos() {
      return this.homePos;
   }

   public boolean hasEgg() {
      return this.entityData.get(HAS_EGG);
   }

   private void setHasEgg(final boolean onOff) {
      this.entityData.set(HAS_EGG, onOff);
   }

   public boolean isLayingEgg() {
      return this.entityData.get(LAYING_EGG);
   }

   private void setLayingEgg(final boolean on) {
      this.layEggCounter = on ? 1 : 0;
      this.entityData.set(LAYING_EGG, on);
   }

   @Override
   protected void defineSynchedData(final SynchedEntityData$Builder entityData) {
      super.defineSynchedData(entityData);
      entityData.define(HAS_EGG, false);
      entityData.define(LAYING_EGG, false);
   }

   @Override
   protected void addAdditionalSaveData(final ValueOutput output) {
      super.addAdditionalSaveData(output);
      output.store("home_pos", BlockPos.CODEC, this.homePos);
      output.putBoolean("has_egg", this.hasEgg());
   }

   @Override
   protected void readAdditionalSaveData(final ValueInput input) {
      this.setHomePos(input.<BlockPos>read("home_pos", BlockPos.CODEC).orElse(this.blockPosition()));
      super.readAdditionalSaveData(input);
      this.setHasEgg(input.getBooleanOr("has_egg", false));
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(
      final ServerLevelAccessor level, final DifficultyInstance difficulty, final EntitySpawnReason spawnReason, @Nullable final SpawnGroupData groupData
   ) {
      this.setHomePos(this.blockPosition());
      return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
   }

   public static boolean checkTurtleSpawnRules(
      final EntityType<Turtle> type, final LevelAccessor level, final EntitySpawnReason spawnReason, final BlockPos pos, final RandomSource random
   ) {
      return pos.getY() < level.getSeaLevel() + 4 && TurtleEggBlock.onSand(level, pos) && isBrightEnoughToSpawn(level, pos);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(0, new Turtle$TurtlePanicGoal(this, 1.2));
      this.goalSelector.addGoal(1, new Turtle$TurtleBreedGoal(this, 1.0));
      this.goalSelector.addGoal(1, new Turtle$TurtleLayEggGoal(this, 1.0));
      this.goalSelector.addGoal(2, new TemptGoal(this, 1.1, i -> i.is(ItemTags.TURTLE_FOOD), false));
      this.goalSelector.addGoal(3, new Turtle$TurtleGoToWaterGoal(this, 1.0));
      this.goalSelector.addGoal(4, new Turtle$TurtleGoHomeGoal(this, 1.0));
      this.goalSelector.addGoal(7, new Turtle$TurtleTravelGoal(this, 1.0));
      this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(9, new Turtle$TurtleRandomStrollGoal(this, 1.0, 100));
   }

   public static AttributeSupplier$Builder createAttributes() {
      return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.STEP_HEIGHT, 1.0);
   }

   @Override
   public boolean isPushedByFluid() {
      return false;
   }

   @Override
   public int getAmbientSoundInterval() {
      return 200;
   }

   @Nullable
   @Override
   protected SoundEvent getAmbientSound() {
      return !this.isInWater() && this.onGround() && !this.isBaby() ? SoundEvents.TURTLE_AMBIENT_LAND : super.getAmbientSound();
   }

   @Override
   protected void playSwimSound(final float volume) {
      super.playSwimSound(volume * 1.5F);
   }

   @Override
   protected SoundEvent getSwimSound() {
      return SoundEvents.TURTLE_SWIM;
   }

   @Nullable
   @Override
   protected SoundEvent getHurtSound(final DamageSource source) {
      return this.isBaby() ? SoundEvents.TURTLE_HURT_BABY : SoundEvents.TURTLE_HURT;
   }

   @Nullable
   @Override
   protected SoundEvent getDeathSound() {
      return this.isBaby() ? SoundEvents.TURTLE_DEATH_BABY : SoundEvents.TURTLE_DEATH;
   }

   @Override
   protected void playStepSound(final BlockPos pos, final BlockState blockState) {
      SoundEvent sound = this.isBaby() ? SoundEvents.TURTLE_SHAMBLE_BABY : SoundEvents.TURTLE_SHAMBLE;
      this.playSound(sound, 0.15F, 1.0F);
   }

   @Override
   public boolean canFallInLove() {
      return super.canFallInLove() && !this.hasEgg();
   }

   @Override
   protected float nextStep() {
      return this.moveDist + 0.15F;
   }

   @Override
   public float getAgeScale() {
      return this.isBaby() ? 0.3F : 1.0F;
   }

   @Override
   protected PathNavigation createNavigation(final Level level) {
      return new Turtle$TurtlePathNavigation(this, level);
   }

   @Nullable
   @Override
   public AgeableMob getBreedOffspring(final ServerLevel level, final AgeableMob partner) {
      return EntityTypes.TURTLE.create(level, EntitySpawnReason.BREEDING);
   }

   @Override
   public boolean isFood(final ItemStack itemStack) {
      return itemStack.is(ItemTags.TURTLE_FOOD);
   }

   @Override
   public float getWalkTargetValue(final BlockPos pos, final LevelReader level) {
      if (!this.goingHome && level.getFluidState(pos).is(FluidTags.WATER)) {
         return 10.0F;
      } else {
         return TurtleEggBlock.onSand(level, pos) ? 10.0F : level.getPathfindingCostFromLightLevels(pos);
      }
   }

   @Override
   public void aiStep() {
      super.aiStep();
      if (this.isAlive() && this.isLayingEgg() && this.layEggCounter >= 1 && this.layEggCounter % 5 == 0) {
         BlockPos pos = this.blockPosition();
         if (TurtleEggBlock.onSand(this.level(), pos)) {
            this.level().levelEvent(2001, pos, Block.getId(this.level().getBlockState(pos.below())));
            this.gameEvent(GameEvent.ENTITY_ACTION);
         }
      }
   }

   @Override
   protected void ageBoundaryReached() {
      super.ageBoundaryReached();
      if (!this.isBaby() && this.level() instanceof ServerLevel level && level.getGameRules().get(GameRules.MOB_DROPS)) {
         this.dropFromGiftLootTable(level, BuiltInLootTables.TURTLE_GROW, this::spawnAtLocation);
      }
   }

   @Override
   protected void travelInWater(final Vec3 input, final double baseGravity, final boolean isFalling, final double oldY) {
      this.moveRelative(0.1F, input);
      this.move(MoverType.SELF, this.getDeltaMovement());
      this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
      if (this.getTarget() == null && (!this.goingHome || !this.homePos.closerToCenterThan(this.position(), 20.0))) {
         this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
      }
   }

   @Override
   public boolean canBeLeashed() {
      return false;
   }

   @Override
   public void thunderHit(final ServerLevel level, final LightningBolt lightningBolt) {
      this.hurtServer(level, this.damageSources().lightningBolt(), Float.MAX_VALUE);
   }

   @Override
   public EntityDimensions getDefaultDimensions(final Pose pose) {
      return this.isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
   }
}
