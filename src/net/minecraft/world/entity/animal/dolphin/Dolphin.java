package net.minecraft.world.entity.animal.dolphin;

import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData$Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AgeableMob$AgeableMobGroupData;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EntityAttachments;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityEvent$Value;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreathAirGoal;
import net.minecraft.world.entity.ai.goal.DolphinJumpGoal;
import net.minecraft.world.entity.ai.goal.FollowPlayerRiddenEntityGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindLiquidGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.AgeableWaterCreature;
import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class Dolphin extends AgeableWaterCreature {
   private static final EntityDataAccessor<Boolean> GOT_FISH = SynchedEntityData.defineId(Dolphin.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Integer> MOISTNESS_LEVEL = SynchedEntityData.defineId(Dolphin.class, EntityDataSerializers.INT);
   private static final TargetingConditions SWIM_WITH_PLAYER_TARGETING = TargetingConditions.forNonCombat().range(10.0).ignoreLineOfSight();
   public static final int TOTAL_AIR_SUPPLY = 4800;
   private static final int TOTAL_MOISTNESS_LEVEL = 2400;
   public static final Predicate<ItemEntity> ALLOWED_ITEMS = e -> !e.hasPickUpDelay() && e.isAlive() && e.isInWater();
   public static final float BABY_SCALE = 0.65F;
   private static final EntityDimensions BABY_DIMENSIONS = EntityTypes.DOLPHIN
      .getDimensions()
      .scale(0.65F)
      .withEyeHeight(0.09375F)
      .withAttachments(EntityAttachments.builder().attach(EntityAttachment.PASSENGER, 0.0F, 0.3125F, 0.0F));
   private static final boolean DEFAULT_GOT_FISH = false;
   @Nullable
   private BlockPos treasurePos;

   public Dolphin(final EntityType<? extends Dolphin> type, final Level level) {
      super(type, level);
      this.moveControl = new SmoothSwimmingMoveControl<>(this, 85, 10, 0.02F, 0.1F, true);
      this.lookControl = new SmoothSwimmingLookControl(this, 10);
      this.setCanPickUpLoot(true);
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(
      final ServerLevelAccessor level, final DifficultyInstance difficulty, final EntitySpawnReason spawnReason, @Nullable final SpawnGroupData groupData
   ) {
      this.setAirSupply(this.getMaxAirSupply());
      this.setXRot(0.0F);
      SpawnGroupData spawnGroupData = Objects.requireNonNullElseGet(groupData, () -> new AgeableMob$AgeableMobGroupData(0.1F));
      return super.finalizeSpawn(level, difficulty, spawnReason, spawnGroupData);
   }

   @Nullable
   public Dolphin getBreedOffspring(final ServerLevel level, final AgeableMob partner) {
      return EntityTypes.DOLPHIN.create(level, EntitySpawnReason.BREEDING);
   }

   @Override
   public float getAgeScale() {
      return this.isBaby() ? 0.65F : 1.0F;
   }

   @Override
   protected void handleAirSupply(final int preTickAirSupply) {
   }

   public boolean gotFish() {
      return this.entityData.get(GOT_FISH);
   }

   public void setGotFish(final boolean gotFish) {
      this.entityData.set(GOT_FISH, gotFish);
   }

   public int getMoistnessLevel() {
      return this.entityData.get(MOISTNESS_LEVEL);
   }

   public void setMoisntessLevel(final int level) {
      this.entityData.set(MOISTNESS_LEVEL, level);
   }

   @Override
   protected void defineSynchedData(final SynchedEntityData$Builder entityData) {
      super.defineSynchedData(entityData);
      entityData.define(GOT_FISH, false);
      entityData.define(MOISTNESS_LEVEL, 2400);
   }

   @Override
   protected void addAdditionalSaveData(final ValueOutput output) {
      super.addAdditionalSaveData(output);
      output.putBoolean("GotFish", this.gotFish());
      output.putInt("Moistness", this.getMoistnessLevel());
   }

   @Override
   protected void readAdditionalSaveData(final ValueInput input) {
      super.readAdditionalSaveData(input);
      this.setGotFish(input.getBooleanOr("GotFish", false));
      this.setMoisntessLevel(input.getIntOr("Moistness", 2400));
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(0, new BreathAirGoal(this));
      this.goalSelector.addGoal(0, new TryFindLiquidGoal(this, FluidTags.DOLPHIN_TRIES_TO_FIND));
      this.goalSelector.addGoal(1, new Dolphin$DolphinSwimToTreasureGoal(this));
      this.goalSelector.addGoal(2, new Dolphin$DolphinSwimWithPlayerGoal(this, 4.0));
      this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0, 10));
      this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
      this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
      this.goalSelector.addGoal(5, new DolphinJumpGoal(this, 10));
      this.goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.2F, true));
      this.goalSelector.addGoal(7, new Dolphin$MoveToItemGoal(this));
      this.goalSelector.addGoal(8, new Dolphin$PlayWithItemsGoal(this));
      this.goalSelector.addGoal(8, new FollowPlayerRiddenEntityGoal(this, AbstractBoat.class));
      this.goalSelector.addGoal(8, new FollowPlayerRiddenEntityGoal(this, AbstractNautilus.class));
      this.goalSelector.addGoal(9, new AvoidEntityGoal<>(this, Guardian.class, 8.0F, 1.0, 1.0));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Guardian.class).setAlertOthers());
   }

   public static AttributeSupplier$Builder createAttributes() {
      return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 1.2F).add(Attributes.ATTACK_DAMAGE, 3.0);
   }

   @Override
   protected PathNavigation createNavigation(final Level level) {
      return new WaterBoundPathNavigation(this, level);
   }

   @Override
   public void playAttackSound() {
      this.playSound(SoundEvents.DOLPHIN_ATTACK, 1.0F, 1.0F);
   }

   @Override
   public boolean canAttack(final LivingEntity target) {
      return !this.isBaby() && super.canAttack(target);
   }

   @Override
   public int getMaxAirSupply() {
      return 4800;
   }

   @Override
   protected int increaseAirSupply(final int currentSupply) {
      return this.getMaxAirSupply();
   }

   @Override
   public int getMaxHeadXRot() {
      return 1;
   }

   @Override
   public int getMaxHeadYRot() {
      return 1;
   }

   @Override
   protected boolean canDispenserEquipIntoSlot(final EquipmentSlot slot) {
      return slot == EquipmentSlot.MAINHAND && this.canPickUpLoot();
   }

   @Override
   protected void pickUpItem(final ServerLevel level, final ItemEntity entity) {
      if (this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
         ItemStack itemStack = entity.getItem();
         if (this.canHoldItem(itemStack)) {
            this.onItemPickup(entity);
            this.setItemSlot(EquipmentSlot.MAINHAND, itemStack);
            this.setGuaranteedDrop(EquipmentSlot.MAINHAND);
            this.take(entity, itemStack.getCount());
            entity.discard();
         }
      }
   }

   @Override
   public void tick() {
      super.tick();
      if (this.isNoAi()) {
         this.setAirSupply(this.getMaxAirSupply());
      } else {
         if (this.isInWaterOrRain()) {
            this.setMoisntessLevel(2400);
         } else {
            this.setMoisntessLevel(this.getMoistnessLevel() - 1);
            if (this.getMoistnessLevel() <= 0) {
               this.hurt(this.damageSources().dryOut(), 1.0F);
            }

            if (this.onGround()) {
               this.setDeltaMovement(
                  this.getDeltaMovement().add((this.random.nextFloat() * 2.0F - 1.0F) * 0.2F, 0.5, (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F)
               );
               this.setYRot(this.random.nextFloat() * 360.0F);
               this.setOnGround(false);
               this.needsSync = true;
            }
         }

         if (this.level().isClientSide() && this.isInWater() && this.getDeltaMovement().lengthSqr() > 0.03) {
            Vec3 viewVector = this.getViewVector(0.0F);
            float c = Mth.cos(this.getYRot() * (float) (Math.PI / 180.0)) * 0.3F;
            float s = Mth.sin(this.getYRot() * (float) (Math.PI / 180.0)) * 0.3F;
            float multiplier = 1.2F - this.random.nextFloat() * 0.7F;

            for (int i = 0; i < 2; i++) {
               this.level()
                  .addParticle(
                     ParticleTypes.DOLPHIN,
                     this.getX() - viewVector.x * multiplier + c,
                     this.getY() - viewVector.y,
                     this.getZ() - viewVector.z * multiplier + s,
                     0.0,
                     0.0,
                     0.0
                  );
               this.level()
                  .addParticle(
                     ParticleTypes.DOLPHIN,
                     this.getX() - viewVector.x * multiplier - c,
                     this.getY() - viewVector.y,
                     this.getZ() - viewVector.z * multiplier - s,
                     0.0,
                     0.0,
                     0.0
                  );
            }
         }
      }
   }

   @Override
   public void handleEntityEvent(@EntityEvent$Value final byte id) {
      if (id == 38) {
         this.addParticlesAroundSelf(ParticleTypes.HAPPY_VILLAGER);
      } else {
         super.handleEntityEvent(id);
      }
   }

   private void addParticlesAroundSelf(final ParticleOptions particle) {
      for (int i = 0; i < 7; i++) {
         double xa = this.random.nextGaussian() * 0.01;
         double ya = this.random.nextGaussian() * 0.01;
         double za = this.random.nextGaussian() * 0.01;
         this.level().addParticle(particle, this.getRandomX(1.0), this.getRandomY() + 0.2, this.getRandomZ(1.0), xa, ya, za);
      }
   }

   @Override
   protected InteractionResult mobInteract(final Player player, final InteractionHand hand) {
      ItemStack itemStack = player.getItemInHand(hand);
      if (!itemStack.isEmpty() && itemStack.is(ItemTags.FISHES)) {
         if (!this.level().isClientSide()) {
            this.playSound(SoundEvents.DOLPHIN_EAT, 1.0F, 1.0F);
         }

         if (this.canAgeUp()) {
            itemStack.consume(1, player);
            this.ageUp(getSpeedUpSecondsWhenFeeding(-this.age), true);
         } else {
            this.setGotFish(true);
            itemStack.consume(1, player);
         }

         return InteractionResult.SUCCESS;
      } else {
         return super.mobInteract(player, hand);
      }
   }

   @Override
   protected SoundEvent getHurtSound(final DamageSource source) {
      return SoundEvents.DOLPHIN_HURT;
   }

   @Nullable
   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.DOLPHIN_DEATH;
   }

   @Nullable
   @Override
   protected SoundEvent getAmbientSound() {
      return this.isInWater() ? SoundEvents.DOLPHIN_AMBIENT_WATER : SoundEvents.DOLPHIN_AMBIENT;
   }

   @Override
   protected SoundEvent getSwimSplashSound() {
      return SoundEvents.DOLPHIN_SPLASH;
   }

   @Override
   protected SoundEvent getSwimSound() {
      return SoundEvents.DOLPHIN_SWIM;
   }

   protected boolean closeToNextPos() {
      BlockPos target = this.getNavigation().getTargetPos();
      return target != null ? target.closerToCenterThan(this.position(), 12.0) : false;
   }

   @Override
   public EntityDimensions getDefaultDimensions(final Pose pose) {
      return this.isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
   }

   @Override
   protected void travelInWater(final Vec3 input, final double baseGravity, final boolean isFalling, final double oldY) {
      this.moveRelative(this.getSpeed(), input);
      this.move(MoverType.SELF, this.getDeltaMovement());
      this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
      if (this.getTarget() == null) {
         this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
      }
   }

   @Override
   public boolean canBeLeashed() {
      return true;
   }
}
