package net.minecraft.world.entity.animal.fox;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData$Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EntityAttachments;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityEvent$Value;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.fish.AbstractFish;
import net.minecraft.world.entity.animal.fish.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.polarbear.PolarBear;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.animal.turtle.Turtle;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class Fox extends Animal {
   private static final EntityDataAccessor<Integer> DATA_TYPE_ID = SynchedEntityData.defineId(Fox.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Fox.class, EntityDataSerializers.BYTE);
   private static final float BABY_SCALE = 0.6F;
   private static final int FLAG_SITTING = 1;
   public static final int FLAG_CROUCHING = 4;
   public static final int FLAG_INTERESTED = 8;
   public static final int FLAG_POUNCING = 16;
   private static final int FLAG_SLEEPING = 32;
   private static final int FLAG_FACEPLANTED = 64;
   private static final int FLAG_DEFENDING = 128;
   private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_TRUSTED_ID_0 = SynchedEntityData.defineId(
      Fox.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE
   );
   private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_TRUSTED_ID_1 = SynchedEntityData.defineId(
      Fox.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE
   );
   private static final Predicate<ItemEntity> ALLOWED_ITEMS = e -> !e.hasPickUpDelay() && e.isAlive();
   private static final Predicate<Entity> TRUSTED_TARGET_SELECTOR = entity -> !(entity instanceof LivingEntity livingEntity)
      ? false
      : livingEntity.getLastHurtMob() != null && livingEntity.getLastHurtMobTimestamp() < livingEntity.tickCount + 600;
   private static final Predicate<Entity> STALKABLE_PREY = entity -> entity instanceof Chicken || entity instanceof Rabbit;
   private static final Predicate<Entity> AVOID_PLAYERS = entity -> !entity.isDiscrete() && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity);
   private static final int MIN_TICKS_BEFORE_EAT = 600;
   private static final EntityDimensions BABY_DIMENSIONS = EntityTypes.FOX
      .getDimensions()
      .scale(0.6F)
      .withEyeHeight(0.34375F)
      .withAttachments(EntityAttachments.builder().attach(EntityAttachment.PASSENGER, 0.0F, 0.375F, 0.0F));
   private static final Codec<List<EntityReference<LivingEntity>>> TRUSTED_LIST_CODEC = EntityReference.codec().listOf();
   private static final boolean DEFAULT_SLEEPING = false;
   private static final boolean DEFAULT_SITTING = false;
   private static final boolean DEFAULT_CROUCHING = false;
   private Goal landTargetGoal;
   private Goal turtleEggTargetGoal;
   private Goal fishTargetGoal;
   private float interestedAngle;
   private float interestedAngleO;
   private float crouchAmount;
   private float crouchAmountO;
   private static final float MAX_CROUCH_AMOUNT = 5.0F;
   private int ticksSinceEaten;

   public Fox(final EntityType<? extends Fox> type, final Level level) {
      super(type, level);
      this.lookControl = new Fox$FoxLookControl(this);
      this.moveControl = new Fox$FoxMoveControl<>(this);
      this.setPathfindingMalus(PathType.DAMAGING_IN_NEIGHBOR, 0.0F);
      this.setPathfindingMalus(PathType.DAMAGING, 0.0F);
      this.setCanPickUpLoot(true);
      this.getNavigation().setRequiredPathLength(32.0F);
   }

   @Override
   protected void defineSynchedData(final SynchedEntityData$Builder entityData) {
      super.defineSynchedData(entityData);
      entityData.define(DATA_TRUSTED_ID_0, Optional.empty());
      entityData.define(DATA_TRUSTED_ID_1, Optional.empty());
      entityData.define(DATA_TYPE_ID, Fox$Variant.DEFAULT.getId());
      entityData.define(DATA_FLAGS_ID, (byte)0);
   }

   @Override
   protected void registerGoals() {
      this.landTargetGoal = new NearestAttackableTargetGoal<>(
         this, Animal.class, 10, false, false, (target, level) -> target instanceof Chicken || target instanceof Rabbit
      );
      this.turtleEggTargetGoal = new NearestAttackableTargetGoal<>(this, Turtle.class, 10, false, false, Turtle.BABY_ON_LAND_SELECTOR);
      this.fishTargetGoal = new NearestAttackableTargetGoal<>(
         this, AbstractFish.class, 20, false, false, (target, level) -> target instanceof AbstractSchoolingFish
      );
      this.goalSelector.addGoal(0, new Fox$FoxFloatGoal(this));
      this.goalSelector.addGoal(0, new ClimbOnTopOfPowderSnowGoal(this, this.level()));
      this.goalSelector.addGoal(1, new Fox$FaceplantGoal(this));
      this.goalSelector.addGoal(2, new Fox$FoxPanicGoal(this, 2.2));
      this.goalSelector.addGoal(3, new Fox$FoxBreedGoal(this, 1.0));
      this.goalSelector
         .addGoal(
            4, new AvoidEntityGoal<>(this, Player.class, 16.0F, 1.6, 1.4, entity -> AVOID_PLAYERS.test(entity) && !this.trusts(entity) && !this.isDefending())
         );
      this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Wolf.class, 8.0F, 1.6, 1.4, entity -> !((Wolf)entity).isTame() && !this.isDefending()));
      this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, PolarBear.class, 8.0F, 1.6, 1.4, entity -> !this.isDefending()));
      this.goalSelector.addGoal(5, new Fox$StalkPreyGoal(this));
      this.goalSelector.addGoal(6, new Fox$FoxPounceGoal(this));
      this.goalSelector.addGoal(6, new Fox$SeekShelterGoal(this, 1.25));
      this.goalSelector.addGoal(7, new Fox$FoxMeleeAttackGoal(this, 1.2F, true));
      this.goalSelector.addGoal(7, new Fox$SleepGoal(this));
      this.goalSelector.addGoal(8, new Fox$FoxFollowParentGoal(this, 1.25));
      this.goalSelector.addGoal(9, new Fox$FoxStrollThroughVillageGoal(this, 32, 200));
      this.goalSelector.addGoal(10, new Fox$FoxEatBerriesGoal(this, 1.2F, 12, 1));
      this.goalSelector.addGoal(10, new LeapAtTargetGoal(this, 0.4F));
      this.goalSelector.addGoal(11, new WaterAvoidingRandomStrollGoal(this, 1.0));
      this.goalSelector.addGoal(11, new Fox$FoxSearchForItemsGoal(this));
      this.goalSelector.addGoal(12, new Fox$FoxLookAtPlayerGoal(this, this, (float)Player.class, 24.0F));
      this.goalSelector.addGoal(13, new Fox$PerchAndSearchGoal(this));
      this.targetSelector
         .addGoal(
            3,
            new Fox$DefendTrustedTargetGoal(
               this, (boolean)LivingEntity.class, false, false, (target, level) -> TRUSTED_TARGET_SELECTOR.test(target) && !this.trusts(target)
            )
         );
   }

   @Override
   public void aiStep() {
      if (!this.level().isClientSide() && this.isAlive() && this.isEffectiveAi()) {
         this.ticksSinceEaten++;
         ItemStack itemInMouth = this.getItemBySlot(EquipmentSlot.MAINHAND);
         if (this.canEat(itemInMouth)) {
            if (this.ticksSinceEaten > 600) {
               ItemStack remainingFood = itemInMouth.finishUsingItem(this.level(), this);
               if (!remainingFood.isEmpty()) {
                  this.setItemSlot(EquipmentSlot.MAINHAND, remainingFood);
               }

               this.ticksSinceEaten = 0;
            } else if (this.ticksSinceEaten > 560 && this.random.nextFloat() < 0.1F) {
               this.playEatingSound();
               this.level().broadcastEntityEvent(this, (byte)45);
            }
         }

         LivingEntity target = this.getTarget();
         if (target == null || !target.isAlive()) {
            this.setIsCrouching(false);
            this.setIsInterested(false);
         }
      }

      if (this.isSleeping() || this.isImmobile()) {
         this.jumping = false;
         this.xxa = 0.0F;
         this.zza = 0.0F;
      }

      super.aiStep();
      if (this.isDefending() && this.random.nextFloat() < 0.05F) {
         this.playSound(SoundEvents.FOX_AGGRO, 1.0F, 1.0F);
      }
   }

   @Override
   protected boolean isImmobile() {
      return this.isDeadOrDying();
   }

   private boolean canEat(final ItemStack itemInMouth) {
      return this.isConsumableFood(itemInMouth) && this.getTarget() == null && this.onGround() && !this.isSleeping();
   }

   private boolean isConsumableFood(final ItemStack itemStack) {
      return itemStack.has(DataComponents.FOOD) && itemStack.has(DataComponents.CONSUMABLE);
   }

   @Override
   protected void populateDefaultEquipmentSlots(final RandomSource random, final DifficultyInstance difficulty) {
      if (random.nextFloat() < 0.2F) {
         float odds = random.nextFloat();
         ItemStack heldInMouth;
         if (odds < 0.05F) {
            heldInMouth = new ItemStack(Items.EMERALD);
         } else if (odds < 0.2F) {
            heldInMouth = new ItemStack(Items.EGG);
         } else if (odds < 0.4F) {
            heldInMouth = random.nextBoolean() ? new ItemStack(Items.RABBIT_FOOT) : new ItemStack(Items.RABBIT_HIDE);
         } else if (odds < 0.6F) {
            heldInMouth = new ItemStack(Items.WHEAT);
         } else if (odds < 0.8F) {
            heldInMouth = new ItemStack(Items.LEATHER);
         } else {
            heldInMouth = new ItemStack(Items.FEATHER);
         }

         this.setItemSlot(EquipmentSlot.MAINHAND, heldInMouth);
      }
   }

   @Override
   public void handleEntityEvent(@EntityEvent$Value final byte id) {
      if (id == 45) {
         ItemStack mouthItem = this.getItemBySlot(EquipmentSlot.MAINHAND);
         if (!mouthItem.isEmpty()) {
            ItemParticleOption breakParticle = new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(mouthItem));

            for (int i = 0; i < 8; i++) {
               Vec3 direction = new Vec3((this.random.nextFloat() - 0.5) * 0.1, this.random.nextFloat() * 0.1 + 0.1, 0.0)
                  .xRot(-this.getXRot() * (float) (Math.PI / 180.0))
                  .yRot(-this.getYRot() * (float) (Math.PI / 180.0));
               this.level()
                  .addParticle(
                     breakParticle,
                     this.getX() + this.getLookAngle().x / 2.0,
                     this.getY(),
                     this.getZ() + this.getLookAngle().z / 2.0,
                     direction.x,
                     direction.y + 0.05,
                     direction.z
                  );
            }
         }
      } else {
         super.handleEntityEvent(id);
      }
   }

   public static AttributeSupplier$Builder createAttributes() {
      return Animal.createAnimalAttributes()
         .add(Attributes.MOVEMENT_SPEED, 0.3F)
         .add(Attributes.MAX_HEALTH, 10.0)
         .add(Attributes.ATTACK_DAMAGE, 2.0)
         .add(Attributes.SAFE_FALL_DISTANCE, 5.0)
         .add(Attributes.FOLLOW_RANGE, 32.0);
   }

   @Nullable
   public Fox getBreedOffspring(final ServerLevel level, final AgeableMob partner) {
      Fox baby = EntityTypes.FOX.create(level, EntitySpawnReason.BREEDING);
      if (baby != null) {
         baby.setVariant(this.random.nextBoolean() ? this.getVariant() : ((Fox)partner).getVariant());
      }

      return baby;
   }

   public static boolean checkFoxSpawnRules(
      final EntityType<Fox> type, final LevelAccessor level, final EntitySpawnReason spawnReason, final BlockPos pos, final RandomSource random
   ) {
      return level.getBlockState(pos.below()).is(BlockTags.FOXES_SPAWNABLE_ON) && isBrightEnoughToSpawn(level, pos);
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(
      final ServerLevelAccessor level, final DifficultyInstance difficulty, final EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData
   ) {
      Holder<Biome> biome = level.getBiome(this.blockPosition());
      Fox$Variant variant = Fox$Variant.byBiome(biome);
      boolean isBaby = false;
      if (groupData instanceof Fox$FoxGroupData foxGroupData) {
         variant = foxGroupData.variant;
         if (foxGroupData.getGroupSize() >= 2) {
            isBaby = true;
         }
      } else {
         groupData = new Fox$FoxGroupData(variant);
      }

      this.setVariant(variant);
      if (isBaby) {
         this.setAge(-24000);
      }

      if (level instanceof ServerLevel) {
         this.setTargetGoals();
      }

      this.populateDefaultEquipmentSlots(level.getRandom(), difficulty);
      return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
   }

   private void setTargetGoals() {
      if (this.getVariant() == Fox$Variant.RED) {
         this.targetSelector.addGoal(4, this.landTargetGoal);
         this.targetSelector.addGoal(4, this.turtleEggTargetGoal);
         this.targetSelector.addGoal(6, this.fishTargetGoal);
      } else {
         this.targetSelector.addGoal(4, this.fishTargetGoal);
         this.targetSelector.addGoal(6, this.landTargetGoal);
         this.targetSelector.addGoal(6, this.turtleEggTargetGoal);
      }
   }

   @Override
   protected void playEatingSound() {
      this.playSound(SoundEvents.FOX_EAT, 1.0F, 1.0F);
   }

   @Override
   public EntityDimensions getDefaultDimensions(final Pose pose) {
      return this.isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
   }

   public Fox$Variant getVariant() {
      return Fox$Variant.byId(this.entityData.get(DATA_TYPE_ID));
   }

   private void setVariant(final Fox$Variant variant) {
      this.entityData.set(DATA_TYPE_ID, variant.getId());
   }

   @Nullable
   @Override
   public <T> T get(final DataComponentType<? extends T> type) {
      return type == DataComponents.FOX_VARIANT ? castComponentValue((DataComponentType<T>)type, this.getVariant()) : super.get(type);
   }

   @Override
   protected void applyImplicitComponents(final DataComponentGetter components) {
      this.applyImplicitComponentIfPresent(components, DataComponents.FOX_VARIANT);
      super.applyImplicitComponents(components);
   }

   @Override
   protected <T> boolean applyImplicitComponent(final DataComponentType<T> type, final T value) {
      if (type == DataComponents.FOX_VARIANT) {
         this.setVariant(castComponentValue(DataComponents.FOX_VARIANT, value));
         return true;
      } else {
         return super.applyImplicitComponent(type, value);
      }
   }

   private Stream<EntityReference<LivingEntity>> getTrustedEntities() {
      return Stream.concat(this.entityData.get(DATA_TRUSTED_ID_0).stream(), this.entityData.get(DATA_TRUSTED_ID_1).stream());
   }

   private void addTrustedEntity(final LivingEntity entity) {
      this.addTrustedEntity(EntityReference.of(entity));
   }

   private void addTrustedEntity(final EntityReference<LivingEntity> reference) {
      if (this.entityData.get(DATA_TRUSTED_ID_0).isPresent()) {
         this.entityData.set(DATA_TRUSTED_ID_1, Optional.of(reference));
      } else {
         this.entityData.set(DATA_TRUSTED_ID_0, Optional.of(reference));
      }
   }

   @Override
   protected void addAdditionalSaveData(final ValueOutput output) {
      super.addAdditionalSaveData(output);
      output.store("Trusted", TRUSTED_LIST_CODEC, this.getTrustedEntities().toList());
      output.putBoolean("Sleeping", this.isSleeping());
      output.store("Type", Fox$Variant.CODEC, this.getVariant());
      output.putBoolean("Sitting", this.isSitting());
      output.putBoolean("Crouching", this.isCrouching());
   }

   @Override
   protected void readAdditionalSaveData(final ValueInput input) {
      super.readAdditionalSaveData(input);
      this.clearTrusted();
      input.<List>read("Trusted", TRUSTED_LIST_CODEC).orElse(List.of()).forEach(this::addTrustedEntity);
      this.setSleeping(input.getBooleanOr("Sleeping", false));
      this.setVariant(input.<Fox$Variant>read("Type", Fox$Variant.CODEC).orElse(Fox$Variant.DEFAULT));
      this.setSitting(input.getBooleanOr("Sitting", false));
      this.setIsCrouching(input.getBooleanOr("Crouching", false));
      if (this.level() instanceof ServerLevel) {
         this.setTargetGoals();
      }
   }

   private void clearTrusted() {
      this.entityData.set(DATA_TRUSTED_ID_0, Optional.empty());
      this.entityData.set(DATA_TRUSTED_ID_1, Optional.empty());
   }

   public boolean isSitting() {
      return this.getFlag(1);
   }

   public void setSitting(final boolean value) {
      this.setFlag(1, value);
   }

   public boolean isFaceplanted() {
      return this.getFlag(64);
   }

   private void setFaceplanted(final boolean faceplanted) {
      this.setFlag(64, faceplanted);
   }

   private boolean isDefending() {
      return this.getFlag(128);
   }

   private void setDefending(final boolean defending) {
      this.setFlag(128, defending);
   }

   @Override
   public boolean isSleeping() {
      return this.getFlag(32);
   }

   private void setSleeping(final boolean sleeping) {
      this.setFlag(32, sleeping);
   }

   private void setFlag(final int flag, final boolean value) {
      if (value) {
         this.entityData.set(DATA_FLAGS_ID, (byte)(this.entityData.get(DATA_FLAGS_ID) | flag));
      } else {
         this.entityData.set(DATA_FLAGS_ID, (byte)(this.entityData.get(DATA_FLAGS_ID) & ~flag));
      }
   }

   private boolean getFlag(final int flag) {
      return (this.entityData.get(DATA_FLAGS_ID) & flag) != 0;
   }

   @Override
   protected boolean canDispenserEquipIntoSlot(final EquipmentSlot slot) {
      return slot == EquipmentSlot.MAINHAND && this.canPickUpLoot();
   }

   @Override
   public boolean canHoldItem(final ItemStack itemStack) {
      ItemStack heldItemStack = this.getItemBySlot(EquipmentSlot.MAINHAND);
      return heldItemStack.isEmpty() || this.ticksSinceEaten > 0 && this.isConsumableFood(itemStack) && !this.isConsumableFood(heldItemStack);
   }

   private void spitOutItem(final ItemStack itemStack) {
      if (!itemStack.isEmpty() && !this.level().isClientSide()) {
         ItemEntity thrownItem = new ItemEntity(
            this.level(), this.getX() + this.getLookAngle().x, this.getY() + 1.0, this.getZ() + this.getLookAngle().z, itemStack
         );
         thrownItem.setPickUpDelay(40);
         thrownItem.setThrower(this);
         this.playSound(SoundEvents.FOX_SPIT, 1.0F, 1.0F);
         this.level().addFreshEntity(thrownItem);
      }
   }

   private void dropItemStack(final ItemStack itemStack) {
      ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), itemStack);
      this.level().addFreshEntity(itemEntity);
   }

   @Override
   protected void pickUpItem(final ServerLevel level, final ItemEntity entity) {
      ItemStack itemStack = entity.getItem();
      if (this.canHoldItem(itemStack)) {
         int count = itemStack.getCount();
         if (count > 1) {
            this.dropItemStack(itemStack.split(count - 1));
         }

         this.spitOutItem(this.getItemBySlot(EquipmentSlot.MAINHAND));
         this.onItemPickup(entity);
         this.setItemSlot(EquipmentSlot.MAINHAND, itemStack.split(1));
         this.setGuaranteedDrop(EquipmentSlot.MAINHAND);
         this.take(entity, itemStack.getCount());
         entity.discard();
         this.ticksSinceEaten = 0;
      }
   }

   @Override
   public void tick() {
      super.tick();
      if (this.isEffectiveAi()) {
         boolean inWater = this.isInWater();
         if (inWater || this.getTarget() != null || this.level().isThundering()) {
            this.wakeUp();
         }

         if (inWater || this.isSleeping()) {
            this.setSitting(false);
         }

         if (this.isFaceplanted() && this.level().getRandom().nextFloat() < 0.2F) {
            BlockPos pos = this.blockPosition();
            BlockState state = this.level().getBlockState(pos);
            this.level().levelEvent(2001, pos, Block.getId(state));
         }
      }

      this.interestedAngleO = this.interestedAngle;
      if (this.isInterested()) {
         this.interestedAngle = this.interestedAngle + (1.0F - this.interestedAngle) * 0.4F;
      } else {
         this.interestedAngle = this.interestedAngle + (0.0F - this.interestedAngle) * 0.4F;
      }

      this.crouchAmountO = this.crouchAmount;
      if (this.isCrouching()) {
         this.crouchAmount += 0.2F;
         if (this.crouchAmount > 5.0F) {
            this.crouchAmount = 5.0F;
         }
      } else {
         this.crouchAmount = 0.0F;
      }
   }

   @Override
   public boolean isFood(final ItemStack itemStack) {
      return itemStack.is(ItemTags.FOX_FOOD);
   }

   @Override
   protected void onOffspringSpawnedFromEgg(final Player spawner, final Mob offspring) {
      ((Fox)offspring).addTrustedEntity(spawner);
   }

   public boolean isPouncing() {
      return this.getFlag(16);
   }

   public void setIsPouncing(final boolean pouncing) {
      this.setFlag(16, pouncing);
   }

   public boolean isFullyCrouched() {
      return this.crouchAmount == 5.0F;
   }

   public void setIsCrouching(final boolean isCrouching) {
      this.setFlag(4, isCrouching);
   }

   @Override
   public boolean isCrouching() {
      return this.getFlag(4);
   }

   public void setIsInterested(final boolean value) {
      this.setFlag(8, value);
   }

   public boolean isInterested() {
      return this.getFlag(8);
   }

   public float getHeadRollAngle(final float a) {
      return Mth.lerp(a, this.interestedAngleO, this.interestedAngle) * 0.11F * (float) Math.PI;
   }

   public float getCrouchAmount(final float a) {
      return Mth.lerp(a, this.crouchAmountO, this.crouchAmount);
   }

   @Override
   public void setTarget(@Nullable final LivingEntity target) {
      if (this.isDefending() && target == null) {
         this.setDefending(false);
      }

      super.setTarget(target);
   }

   private void wakeUp() {
      this.setSleeping(false);
   }

   private void clearStates() {
      this.setIsInterested(false);
      this.setIsCrouching(false);
      this.setSitting(false);
      this.setSleeping(false);
      this.setDefending(false);
      this.setFaceplanted(false);
   }

   public boolean canMove() {
      return !this.isSleeping() && !this.isSitting() && !this.isFaceplanted();
   }

   @Override
   public void playAmbientSound() {
      SoundEvent ambient = this.getAmbientSound();
      if (ambient == SoundEvents.FOX_SCREECH) {
         this.playSound(ambient, 2.0F, this.getVoicePitch());
      } else {
         super.playAmbientSound();
      }
   }

   @Nullable
   @Override
   protected SoundEvent getAmbientSound() {
      if (this.isSleeping()) {
         return SoundEvents.FOX_SLEEP;
      } else {
         if (!this.level().isBrightOutside() && this.random.nextFloat() < 0.1F) {
            List<Player> nearbyEntities = this.level()
               .getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(16.0, 16.0, 16.0), EntitySelector.NO_SPECTATORS);
            if (nearbyEntities.isEmpty()) {
               return SoundEvents.FOX_SCREECH;
            }
         }

         return SoundEvents.FOX_AMBIENT;
      }
   }

   @Nullable
   @Override
   protected SoundEvent getHurtSound(final DamageSource source) {
      return SoundEvents.FOX_HURT;
   }

   @Nullable
   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.FOX_DEATH;
   }

   private boolean trusts(final LivingEntity entity) {
      return this.getTrustedEntities().anyMatch(trusted -> trusted.matches(entity));
   }

   @Override
   protected void dropAllDeathLoot(final ServerLevel level, final DamageSource source) {
      ItemStack itemStack = this.getItemBySlot(EquipmentSlot.MAINHAND);
      if (!itemStack.isEmpty()) {
         this.spawnAtLocation(level, itemStack);
         this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
      }

      super.dropAllDeathLoot(level, source);
   }

   public static boolean isPathClear(final Fox fox, final LivingEntity target) {
      double zdiff = target.getZ() - fox.getZ();
      double xdiff = target.getX() - fox.getX();
      double slope = zdiff / xdiff;
      int increments = 6;

      for (int i = 0; i < 6; i++) {
         double z = slope == 0.0 ? 0.0 : zdiff * (i / 6.0F);
         double x = slope == 0.0 ? xdiff * (i / 6.0F) : z / slope;

         for (int j = 1; j < 4; j++) {
            if (!fox.level().getBlockState(BlockPos.containing(fox.getX() + x, fox.getY() + j, fox.getZ() + z)).canBeReplaced()) {
               return false;
            }
         }
      }

      return true;
   }

   @Override
   public Vec3 getLeashOffset() {
      return new Vec3(0.0, 0.55F * this.getEyeHeight(), this.getBbWidth() * 0.4F);
   }
}
