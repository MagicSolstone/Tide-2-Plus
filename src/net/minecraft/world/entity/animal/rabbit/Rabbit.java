package net.minecraft.world.entity.animal.rabbit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData$Builder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityEvent$Value;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.Leashable$LeashData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class Rabbit extends Animal {
   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.24F, 0.4F).withEyeHeight(0.39F);
   public static final double STROLL_SPEED_MOD = 0.6;
   public static final double BREED_SPEED_MOD = 0.8;
   public static final double FOLLOW_SPEED_MOD = 1.0;
   public static final double FLEE_SPEED_MOD = 2.2;
   public static final double ATTACK_SPEED_MOD = 1.4;
   private static final double BABY_JUMP_HEIGHT = 0.5;
   private static final double ADULT_JUMP_HEIGHT = 1.5;
   private static final int JUMP_DELAY_TICKS = 10;
   private static final int PANIC_JUMP_DELAY_TICKS = 3;
   private static final int JUMP_DURATION_IN_TICKS = 15;
   private static final EntityDataAccessor<Integer> DATA_TYPE_ID = SynchedEntityData.defineId(Rabbit.class, EntityDataSerializers.INT);
   private static final int DEFAULT_MORE_CARROT_TICKS = 0;
   public final AnimationState hopAnimationState = new AnimationState();
   public final AnimationState idleHeadTiltAnimationState = new AnimationState();
   private static final int IDLE_MINIMAL_DURATION_TICKS = 180;
   private int idleAnimationTimeout = this.random.nextInt(40) + 180;
   private static final Identifier KILLER_BUNNY = Identifier.withDefaultNamespace("killer_bunny");
   private static final int DEFAULT_ATTACK_POWER = 3;
   private static final int EVIL_ATTACK_POWER_INCREMENT = 5;
   private static final Identifier EVIL_ATTACK_POWER_MODIFIER = Identifier.withDefaultNamespace("evil");
   private static final int EVIL_ARMOR_VALUE = 8;
   private static final int MORE_CARROTS_DELAY = 40;
   private int jumpTicks;
   private int jumpDuration;
   private boolean wasOnGround;
   private int jumpDelayTicks;
   private int moreCarrotTicks = 0;

   public Rabbit(final EntityType<? extends Rabbit> type, final Level level) {
      super(type, level);
      this.jumpControl = new Rabbit$RabbitJumpControl(this);
      this.moveControl = new Rabbit$RabbitMoveControl<>(this);
      this.setSpeedModifier(0.0);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(1, new FloatGoal(this));
      this.goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, this.level()));
      this.goalSelector.addGoal(1, new Rabbit$RabbitPanicGoal(this, 2.2));
      this.goalSelector.addGoal(2, new BreedGoal(this, 0.8));
      this.goalSelector.addGoal(3, new TemptGoal(this, 1.0, i -> i.is(ItemTags.RABBIT_FOOD), false));
      this.goalSelector.addGoal(4, new Rabbit$RabbitAvoidEntityGoal<>(this, Player.class, 8.0F, 2.2, 2.2));
      this.goalSelector.addGoal(4, new Rabbit$RabbitAvoidEntityGoal<>(this, Wolf.class, 10.0F, 2.2, 2.2));
      this.goalSelector.addGoal(4, new Rabbit$RabbitAvoidEntityGoal<>(this, Monster.class, 4.0F, 2.2, 2.2));
      this.goalSelector.addGoal(5, new Rabbit$RaidGardenGoal(this));
      this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.6));
      this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 10.0F));
   }

   @Override
   protected EntityDimensions getDefaultDimensions(final Pose pose) {
      return this.isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
   }

   @Override
   protected float getJumpPower() {
      float baseJumpPower = 0.3F;
      if (this.moveControl.getSpeedModifier() <= 0.6) {
         baseJumpPower = 0.2F;
      }

      Path path = this.navigation.getPath();
      if (path != null && !path.isDone()) {
         Vec3 currentPos = path.getNextEntityPos(this);
         if (currentPos.y > this.getY() + 0.5) {
            baseJumpPower = 0.5F;
         }
      }

      if (this.horizontalCollision || this.jumping && this.moveControl.getWantedY() > this.getY() + 0.5) {
         baseJumpPower = 0.5F;
      }

      return super.getJumpPower(baseJumpPower / 0.42F);
   }

   @Override
   public void jumpFromGround() {
      super.jumpFromGround();
      double speedModifier = this.moveControl.getSpeedModifier();
      if (speedModifier > 0.0) {
         double current = this.getDeltaMovement().horizontalDistanceSqr();
         if (current < 0.01) {
            this.moveRelative(0.1F, new Vec3(0.0, this.isBaby() ? 0.5 : 1.5, 1.0));
         }
      }

      if (!this.level().isClientSide()) {
         this.level().broadcastEntityEvent(this, (byte)1);
      }
   }

   public float getJumpCompletion(final float a) {
      return this.jumpDuration == 0 ? 0.0F : (this.jumpTicks + a) / this.jumpDuration;
   }

   public void setSpeedModifier(final double speed) {
      this.getNavigation().setSpeedModifier(speed);
      this.moveControl.setWantedPosition(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ(), speed);
   }

   @Override
   public void setJumping(final boolean jump) {
      super.setJumping(jump);
      if (jump) {
         this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
      }
   }

   public void startJumping() {
      this.setJumping(true);
      this.jumpDuration = 15;
      this.jumpTicks = 0;
   }

   @Override
   protected void defineSynchedData(final SynchedEntityData$Builder entityData) {
      super.defineSynchedData(entityData);
      entityData.define(DATA_TYPE_ID, Rabbit$Variant.DEFAULT.id);
   }

   @Override
   public void customServerAiStep(final ServerLevel level) {
      if (this.jumpDelayTicks > 0) {
         this.jumpDelayTicks--;
      }

      if (this.moreCarrotTicks > 0) {
         this.moreCarrotTicks = this.moreCarrotTicks - this.random.nextInt(3);
         if (this.moreCarrotTicks < 0) {
            this.moreCarrotTicks = 0;
         }
      }

      if (this.onGround()) {
         if (!this.wasOnGround) {
            this.setJumping(false);
            this.checkLandingDelay();
         }

         if (this.getVariant() == Rabbit$Variant.EVIL && this.jumpDelayTicks == 0) {
            LivingEntity target = this.getTarget();
            if (target != null && this.distanceToSqr(target) < 16.0) {
               this.facePoint(target.getX(), target.getZ());
               this.moveControl.setWantedPosition(target.getX(), target.getY(), target.getZ(), this.moveControl.getSpeedModifier());
               this.startJumping();
               this.wasOnGround = true;
            }
         }

         Rabbit$RabbitJumpControl jumpControl = (Rabbit$RabbitJumpControl)this.jumpControl;
         if (!jumpControl.wantJump()) {
            if (this.moveControl.hasWanted() && this.jumpDelayTicks == 0) {
               Path path = this.navigation.getPath();
               Vec3 pos = new Vec3(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ());
               if (path != null && !path.isDone()) {
                  pos = path.getNextEntityPos(this);
               }

               this.facePoint(pos.x, pos.z);
               this.startJumping();
            }
         } else if (!jumpControl.canJump()) {
            this.enableJumpControl();
         }
      }

      this.wasOnGround = this.onGround();
   }

   @Override
   public boolean canSpawnSprintParticle() {
      return false;
   }

   private void facePoint(final double faceX, final double faceZ) {
      this.setYRot((float)(Mth.atan2(faceZ - this.getZ(), faceX - this.getX()) * 180.0F / (float)Math.PI) - 90.0F);
   }

   private void enableJumpControl() {
      ((Rabbit$RabbitJumpControl)this.jumpControl).setCanJump(true);
   }

   private void disableJumpControl() {
      ((Rabbit$RabbitJumpControl)this.jumpControl).setCanJump(false);
   }

   private void setLandingDelay() {
      this.jumpDelayTicks = this.moveControl.getSpeedModifier() < 2.2 ? 10 : 3;
   }

   private void checkLandingDelay() {
      this.setLandingDelay();
      this.disableJumpControl();
   }

   @Override
   public void aiStep() {
      super.aiStep();
      if (this.jumpTicks != this.jumpDuration) {
         this.jumpTicks++;
      } else if (this.jumpDuration != 0) {
         this.jumpTicks = 0;
         this.jumpDuration = 0;
         this.setJumping(false);
      }
   }

   public static AttributeSupplier$Builder createAttributes() {
      return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 3.0).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.ATTACK_DAMAGE, 3.0);
   }

   @Override
   protected void addAdditionalSaveData(final ValueOutput output) {
      super.addAdditionalSaveData(output);
      output.store("RabbitType", Rabbit$Variant.LEGACY_CODEC, this.getVariant());
      output.putInt("MoreCarrotTicks", this.moreCarrotTicks);
   }

   @Override
   protected void readAdditionalSaveData(final ValueInput input) {
      super.readAdditionalSaveData(input);
      this.setVariant(input.<Rabbit$Variant>read("RabbitType", Rabbit$Variant.LEGACY_CODEC).orElse(Rabbit$Variant.DEFAULT));
      this.moreCarrotTicks = input.getIntOr("MoreCarrotTicks", 0);
   }

   protected SoundEvent getJumpSound() {
      return SoundEvents.RABBIT_JUMP;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.RABBIT_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(final DamageSource source) {
      return SoundEvents.RABBIT_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.RABBIT_DEATH;
   }

   @Override
   public void playAttackSound() {
      if (this.getVariant() == Rabbit$Variant.EVIL) {
         this.playSound(SoundEvents.RABBIT_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
      }
   }

   @Override
   public SoundSource getSoundSource() {
      return this.getVariant() == Rabbit$Variant.EVIL ? SoundSource.HOSTILE : SoundSource.NEUTRAL;
   }

   @Nullable
   public Rabbit getBreedOffspring(final ServerLevel level, final AgeableMob partner) {
      Rabbit offspring = EntityTypes.RABBIT.create(level, EntitySpawnReason.BREEDING);
      if (offspring != null) {
         Rabbit$Variant variant = getRandomRabbitVariant(level, this.blockPosition());
         if (this.random.nextInt(20) != 0) {
            if (partner instanceof Rabbit rabbitPartner && this.random.nextBoolean()) {
               variant = rabbitPartner.getVariant();
            } else {
               variant = this.getVariant();
            }
         }

         offspring.setVariant(variant);
      }

      return offspring;
   }

   @Override
   public boolean isFood(final ItemStack itemStack) {
      return itemStack.is(ItemTags.RABBIT_FOOD);
   }

   public Rabbit$Variant getVariant() {
      return Rabbit$Variant.byId(this.entityData.get(DATA_TYPE_ID));
   }

   private void setVariant(final Rabbit$Variant variant) {
      if (variant == Rabbit$Variant.EVIL) {
         this.getAttribute(Attributes.ARMOR).setBaseValue(8.0);
         this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.4, true));
         this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
         this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
         this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Wolf.class, true));
         this.getAttribute(Attributes.ATTACK_DAMAGE)
            .addOrUpdateTransientModifier(new AttributeModifier(EVIL_ATTACK_POWER_MODIFIER, 5.0, AttributeModifier$Operation.ADD_VALUE));
         if (!this.hasCustomName()) {
            this.setCustomName(Component.translatable(Util.makeDescriptionId("entity", KILLER_BUNNY)));
         }
      } else {
         this.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(EVIL_ATTACK_POWER_MODIFIER);
      }

      this.entityData.set(DATA_TYPE_ID, variant.id);
   }

   @Nullable
   @Override
   public <T> T get(final DataComponentType<? extends T> type) {
      return type == DataComponents.RABBIT_VARIANT ? castComponentValue((DataComponentType<T>)type, this.getVariant()) : super.get(type);
   }

   @Override
   protected void applyImplicitComponents(final DataComponentGetter components) {
      this.applyImplicitComponentIfPresent(components, DataComponents.RABBIT_VARIANT);
      super.applyImplicitComponents(components);
   }

   @Override
   protected <T> boolean applyImplicitComponent(final DataComponentType<T> type, final T value) {
      if (type == DataComponents.RABBIT_VARIANT) {
         this.setVariant(castComponentValue(DataComponents.RABBIT_VARIANT, value));
         return true;
      } else {
         return super.applyImplicitComponent(type, value);
      }
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(
      final ServerLevelAccessor level, final DifficultyInstance difficulty, final EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData
   ) {
      Rabbit$Variant variant = getRandomRabbitVariant(level, this.blockPosition());
      if (groupData instanceof Rabbit$RabbitGroupData) {
         variant = ((Rabbit$RabbitGroupData)groupData).variant;
      } else {
         groupData = new Rabbit$RabbitGroupData(variant);
      }

      this.setVariant(variant);
      return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
   }

   private static Rabbit$Variant getRandomRabbitVariant(final LevelAccessor level, final BlockPos pos) {
      Holder<Biome> biome = level.getBiome(pos);
      int randomVal = level.getRandom().nextInt(100);
      if (biome.is(BiomeTags.SPAWNS_WHITE_RABBITS)) {
         return randomVal < 80 ? Rabbit$Variant.WHITE : Rabbit$Variant.WHITE_SPLOTCHED;
      } else if (biome.is(BiomeTags.SPAWNS_GOLD_RABBITS)) {
         return Rabbit$Variant.GOLD;
      } else {
         return randomVal < 50 ? Rabbit$Variant.BROWN : (randomVal < 90 ? Rabbit$Variant.SALT : Rabbit$Variant.BLACK);
      }
   }

   public static boolean checkRabbitSpawnRules(
      final EntityType<Rabbit> type, final LevelAccessor level, final EntitySpawnReason spawnReason, final BlockPos pos, final RandomSource random
   ) {
      return level.getBlockState(pos.below()).is(BlockTags.RABBITS_SPAWNABLE_ON) && isBrightEnoughToSpawn(level, pos);
   }

   private boolean wantsMoreFood() {
      return this.moreCarrotTicks <= 0;
   }

   @Override
   public void handleEntityEvent(@EntityEvent$Value final byte id) {
      if (id == 1) {
         this.spawnSprintParticle();
         this.jumpDuration = 15;
         this.jumpTicks = 0;
      } else {
         super.handleEntityEvent(id);
      }
   }

   @Override
   public Vec3 getLeashOffset() {
      return new Vec3(0.0, 0.6F * this.getEyeHeight(), this.getBbWidth() * 0.4F);
   }

   private void setupAnimationStates() {
      if (this.shouldPlayIdleAnimation()) {
         this.idleAnimationTimeout = this.random.nextInt(40) + 180;
         this.idleHeadTiltAnimationState.start(this.tickCount);
      } else if (this.jumpTicks > 0) {
         this.hopAnimationState.startIfStopped(this.tickCount);
         this.idleHeadTiltAnimationState.stop();
      } else {
         this.idleAnimationTimeout--;
         this.hopAnimationState.stop();
      }
   }

   private boolean shouldPlayIdleAnimation() {
      return this.idleAnimationTimeout <= 0 && (this.getLeashData() == null || this.getLeashData().leashHolder == null) && !this.isNoAi();
   }

   @Override
   public void setLeashData(@Nullable final Leashable$LeashData leashData) {
      super.setLeashData(leashData);
      this.idleHeadTiltAnimationState.stop();
   }

   @Override
   public void baseTick() {
      super.baseTick();
      if (this.level().isClientSide()) {
         this.setupAnimationStates();
      }
   }
}
