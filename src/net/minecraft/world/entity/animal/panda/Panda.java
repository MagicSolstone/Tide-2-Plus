package net.minecraft.world.entity.animal.panda;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData$Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AgeableMob$AgeableMobGroupData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EntityAttachments;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class Panda extends Animal {
   private static final EntityDataAccessor<Integer> UNHAPPY_COUNTER = SynchedEntityData.defineId(Panda.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> SNEEZE_COUNTER = SynchedEntityData.defineId(Panda.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> EAT_COUNTER = SynchedEntityData.defineId(Panda.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Byte> MAIN_GENE_ID = SynchedEntityData.defineId(Panda.class, EntityDataSerializers.BYTE);
   private static final EntityDataAccessor<Byte> HIDDEN_GENE_ID = SynchedEntityData.defineId(Panda.class, EntityDataSerializers.BYTE);
   private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(Panda.class, EntityDataSerializers.BYTE);
   private static final TargetingConditions BREED_TARGETING = TargetingConditions.forNonCombat().range(8.0);
   private static final EntityDimensions BABY_DIMENSIONS = EntityTypes.PANDA
      .getDimensions()
      .scale(0.5F)
      .withEyeHeight(0.28125F)
      .withAttachments(EntityAttachments.builder().attach(EntityAttachment.PASSENGER, 0.0F, 0.5625F, 0.0F));
   private static final int FLAG_SNEEZE = 2;
   private static final int FLAG_ROLL = 4;
   private static final int FLAG_SIT = 8;
   private static final int FLAG_ON_BACK = 16;
   private static final int EAT_TICK_INTERVAL = 5;
   public static final int TOTAL_ROLL_STEPS = 32;
   private static final int TOTAL_UNHAPPY_TIME = 32;
   private boolean gotBamboo;
   private boolean didBite;
   public int rollCounter;
   private Vec3 rollDelta;
   private float sitAmount;
   private float sitAmountO;
   private float onBackAmount;
   private float onBackAmountO;
   private float rollAmount;
   private float rollAmountO;
   private Panda$PandaLookAtPlayerGoal lookAtPlayerGoal;

   public Panda(final EntityType<? extends Panda> type, final Level level) {
      super(type, level);
      this.moveControl = new Panda$PandaMoveControl<>(this);
      if (!this.isBaby()) {
         this.setCanPickUpLoot(true);
      }
   }

   @Override
   protected boolean canDispenserEquipIntoSlot(final EquipmentSlot slot) {
      return slot == EquipmentSlot.MAINHAND && this.canPickUpLoot();
   }

   public int getUnhappyCounter() {
      return this.entityData.get(UNHAPPY_COUNTER);
   }

   public void setUnhappyCounter(final int value) {
      this.entityData.set(UNHAPPY_COUNTER, value);
   }

   public boolean isSneezing() {
      return this.getFlag(2);
   }

   public boolean isSitting() {
      return this.getFlag(8);
   }

   public void sit(final boolean value) {
      this.setFlag(8, value);
   }

   public boolean isOnBack() {
      return this.getFlag(16);
   }

   public void setOnBack(final boolean value) {
      this.setFlag(16, value);
   }

   public boolean isEating() {
      return this.entityData.get(EAT_COUNTER) > 0;
   }

   public void eat(final boolean value) {
      this.entityData.set(EAT_COUNTER, value ? 1 : 0);
   }

   private int getEatCounter() {
      return this.entityData.get(EAT_COUNTER);
   }

   private void setEatCounter(final int value) {
      this.entityData.set(EAT_COUNTER, value);
   }

   public void sneeze(final boolean value) {
      this.setFlag(2, value);
      if (!value) {
         this.setSneezeCounter(0);
      }
   }

   public int getSneezeCounter() {
      return this.entityData.get(SNEEZE_COUNTER);
   }

   public void setSneezeCounter(final int value) {
      this.entityData.set(SNEEZE_COUNTER, value);
   }

   public Panda$Gene getMainGene() {
      return Panda$Gene.byId(this.entityData.get(MAIN_GENE_ID));
   }

   public void setMainGene(Panda$Gene gene) {
      if (gene.getId() > 6) {
         gene = Panda$Gene.getRandom(this.random);
      }

      this.entityData.set(MAIN_GENE_ID, (byte)gene.getId());
   }

   public Panda$Gene getHiddenGene() {
      return Panda$Gene.byId(this.entityData.get(HIDDEN_GENE_ID));
   }

   public void setHiddenGene(Panda$Gene gene) {
      if (gene.getId() > 6) {
         gene = Panda$Gene.getRandom(this.random);
      }

      this.entityData.set(HIDDEN_GENE_ID, (byte)gene.getId());
   }

   public boolean isRolling() {
      return this.getFlag(4);
   }

   public void roll(final boolean value) {
      this.setFlag(4, value);
   }

   @Override
   protected void defineSynchedData(final SynchedEntityData$Builder entityData) {
      super.defineSynchedData(entityData);
      entityData.define(UNHAPPY_COUNTER, 0);
      entityData.define(SNEEZE_COUNTER, 0);
      entityData.define(MAIN_GENE_ID, (byte)0);
      entityData.define(HIDDEN_GENE_ID, (byte)0);
      entityData.define(DATA_ID_FLAGS, (byte)0);
      entityData.define(EAT_COUNTER, 0);
   }

   private boolean getFlag(final int flag) {
      return (this.entityData.get(DATA_ID_FLAGS) & flag) != 0;
   }

   private void setFlag(final int flag, final boolean value) {
      byte current = this.entityData.get(DATA_ID_FLAGS);
      if (value) {
         this.entityData.set(DATA_ID_FLAGS, (byte)(current | flag));
      } else {
         this.entityData.set(DATA_ID_FLAGS, (byte)(current & ~flag));
      }
   }

   @Override
   protected void addAdditionalSaveData(final ValueOutput output) {
      super.addAdditionalSaveData(output);
      output.store("MainGene", Panda$Gene.CODEC, this.getMainGene());
      output.store("HiddenGene", Panda$Gene.CODEC, this.getHiddenGene());
   }

   @Override
   protected void readAdditionalSaveData(final ValueInput input) {
      super.readAdditionalSaveData(input);
      this.setMainGene(input.<Panda$Gene>read("MainGene", Panda$Gene.CODEC).orElse(Panda$Gene.NORMAL));
      this.setHiddenGene(input.<Panda$Gene>read("HiddenGene", Panda$Gene.CODEC).orElse(Panda$Gene.NORMAL));
   }

   @Nullable
   @Override
   public AgeableMob getBreedOffspring(final ServerLevel level, final AgeableMob partner) {
      Panda baby = EntityTypes.PANDA.create(level, EntitySpawnReason.BREEDING);
      if (baby != null) {
         if (partner instanceof Panda partnerPanda) {
            baby.setGeneFromParents(this, partnerPanda);
         }

         baby.setAttributes();
      }

      return baby;
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(2, new Panda$PandaPanicGoal(this, 2.0));
      this.goalSelector.addGoal(2, new Panda$PandaBreedGoal(this, 1.0));
      this.goalSelector.addGoal(3, new Panda$PandaAttackGoal(this, 1.2F, true));
      this.goalSelector.addGoal(4, new TemptGoal(this, 1.0, i -> i.is(ItemTags.PANDA_FOOD), false));
      this.goalSelector.addGoal(6, new Panda$PandaAvoidGoal<>(this, Player.class, 8.0F, 2.0, 2.0));
      this.goalSelector.addGoal(6, new Panda$PandaAvoidGoal<>(this, Monster.class, 4.0F, 2.0, 2.0));
      this.goalSelector.addGoal(7, new Panda$PandaSitGoal(this));
      this.goalSelector.addGoal(8, new Panda$PandaLieOnBackGoal(this));
      this.goalSelector.addGoal(8, new Panda$PandaSneezeGoal(this));
      this.lookAtPlayerGoal = new Panda$PandaLookAtPlayerGoal(this, Player.class, 6.0F);
      this.goalSelector.addGoal(9, this.lookAtPlayerGoal);
      this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
      this.goalSelector.addGoal(12, new Panda$PandaRollGoal(this));
      this.goalSelector.addGoal(13, new FollowParentGoal(this, 1.25));
      this.goalSelector.addGoal(14, new WaterAvoidingRandomStrollGoal(this, 1.0));
      this.targetSelector.addGoal(1, new Panda$PandaHurtByTargetGoal(this).setAlertOthers(new Class[0]));
   }

   public static AttributeSupplier$Builder createAttributes() {
      return Animal.createAnimalAttributes().add(Attributes.MOVEMENT_SPEED, 0.15F).add(Attributes.ATTACK_DAMAGE, 6.0);
   }

   public Panda$Gene getVariant() {
      return Panda$Gene.getVariantFromGenes(this.getMainGene(), this.getHiddenGene());
   }

   public boolean isLazy() {
      return this.getVariant() == Panda$Gene.LAZY;
   }

   public boolean isWorried() {
      return this.getVariant() == Panda$Gene.WORRIED;
   }

   public boolean isPlayful() {
      return this.getVariant() == Panda$Gene.PLAYFUL;
   }

   public boolean isBrown() {
      return this.getVariant() == Panda$Gene.BROWN;
   }

   public boolean isWeak() {
      return this.getVariant() == Panda$Gene.WEAK;
   }

   @Override
   public boolean isAggressive() {
      return this.getVariant() == Panda$Gene.AGGRESSIVE;
   }

   @Override
   public boolean canBeLeashed() {
      return false;
   }

   @Override
   public boolean doHurtTarget(final ServerLevel level, final Entity target) {
      if (!this.isAggressive()) {
         this.didBite = true;
      }

      return super.doHurtTarget(level, target);
   }

   @Override
   public void playAttackSound() {
      this.playSound(SoundEvents.PANDA_BITE, 1.0F, 1.0F);
   }

   @Override
   public void tick() {
      super.tick();
      if (this.isWorried()) {
         if (this.level().isThundering() && !this.isInWater()) {
            this.sit(true);
            this.eat(false);
         } else if (!this.isEating()) {
            this.sit(false);
         }
      }

      LivingEntity target = this.getTarget();
      if (target == null) {
         this.gotBamboo = false;
         this.didBite = false;
      }

      if (this.getUnhappyCounter() > 0) {
         if (target != null) {
            this.lookAt(target, 90.0F, 90.0F);
         }

         if (this.getUnhappyCounter() == 29 || this.getUnhappyCounter() == 14) {
            this.playSound(SoundEvents.PANDA_CANT_BREED, 1.0F, 1.0F);
         }

         this.setUnhappyCounter(this.getUnhappyCounter() - 1);
      }

      if (this.isSneezing()) {
         this.setSneezeCounter(this.getSneezeCounter() + 1);
         if (this.getSneezeCounter() > 20) {
            this.sneeze(false);
            this.afterSneeze();
         } else if (this.getSneezeCounter() == 1) {
            this.playSound(SoundEvents.PANDA_PRE_SNEEZE, 1.0F, 1.0F);
         }
      }

      if (this.isRolling()) {
         this.handleRoll();
      } else {
         this.rollCounter = 0;
      }

      if (this.isSitting()) {
         this.setXRot(0.0F);
      }

      this.updateSitAmount();
      this.handleEating();
      this.updateOnBackAnimation();
      this.updateRollAmount();
   }

   public boolean isScared() {
      return this.isWorried() && this.level().isThundering();
   }

   private void handleEating() {
      if (!this.isEating() && this.isSitting() && !this.isScared() && !this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && this.random.nextInt(80) == 1) {
         this.eat(true);
      } else if (this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() || !this.isSitting()) {
         this.eat(false);
      }

      if (this.isEating()) {
         this.addEatingParticles();
         if (!this.level().isClientSide() && this.getEatCounter() > 80 && this.random.nextInt(20) == 1) {
            if (this.getEatCounter() > 100 && this.getItemBySlot(EquipmentSlot.MAINHAND).is(ItemTags.PANDA_EATS_FROM_GROUND)) {
               if (!this.level().isClientSide()) {
                  this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                  this.gameEvent(GameEvent.EAT);
               }

               this.sit(false);
            }

            this.eat(false);
            return;
         }

         this.setEatCounter(this.getEatCounter() + 1);
      }
   }

   private void addEatingParticles() {
      if (this.getEatCounter() % 5 == 0) {
         this.playSound(SoundEvents.PANDA_EAT, 0.5F + 0.5F * this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
         ItemStack heldItem = this.getItemBySlot(EquipmentSlot.MAINHAND);
         if (!heldItem.isEmpty()) {
            ItemParticleOption breakParticle = new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(heldItem));

            for (int i = 0; i < 6; i++) {
               Vec3 velocity = new Vec3((this.random.nextFloat() - 0.5) * 0.1, this.random.nextFloat() * 0.1 + 0.1, (this.random.nextFloat() - 0.5) * 0.1)
                  .xRot(-this.getXRot() * (float) (Math.PI / 180.0))
                  .yRot(-this.getYRot() * (float) (Math.PI / 180.0));
               Vec3 position = new Vec3(
                     (this.random.nextFloat() - 0.5) * 0.8, -this.random.nextFloat() * 0.6 - 0.3, 1.0 + (this.random.nextFloat() - 0.5) * 0.4
                  )
                  .yRot(-this.yBodyRot * (float) (Math.PI / 180.0))
                  .add(this.getX(), this.getEyeY() + 1.0, this.getZ());
               this.level().addParticle(breakParticle, position.x, position.y, position.z, velocity.x, velocity.y + 0.05, velocity.z);
            }
         }
      }
   }

   private void updateSitAmount() {
      this.sitAmountO = this.sitAmount;
      if (this.isSitting()) {
         this.sitAmount = Math.min(1.0F, this.sitAmount + 0.15F);
      } else {
         this.sitAmount = Math.max(0.0F, this.sitAmount - 0.19F);
      }
   }

   private void updateOnBackAnimation() {
      this.onBackAmountO = this.onBackAmount;
      if (this.isOnBack()) {
         this.onBackAmount = Math.min(1.0F, this.onBackAmount + 0.15F);
      } else {
         this.onBackAmount = Math.max(0.0F, this.onBackAmount - 0.19F);
      }
   }

   private void updateRollAmount() {
      this.rollAmountO = this.rollAmount;
      if (this.isRolling()) {
         this.rollAmount = Math.min(1.0F, this.rollAmount + 0.15F);
      } else {
         this.rollAmount = Math.max(0.0F, this.rollAmount - 0.19F);
      }
   }

   public float getSitAmount(final float a) {
      return Mth.lerp(a, this.sitAmountO, this.sitAmount);
   }

   public float getLieOnBackAmount(final float a) {
      return Mth.lerp(a, this.onBackAmountO, this.onBackAmount);
   }

   public float getRollAmount(final float a) {
      return Mth.lerp(a, this.rollAmountO, this.rollAmount);
   }

   private void handleRoll() {
      this.rollCounter++;
      if (this.rollCounter > 32) {
         this.roll(false);
      } else {
         if (!this.level().isClientSide()) {
            Vec3 movement = this.getDeltaMovement();
            if (this.rollCounter == 1) {
               float angle = this.getYRot() * (float) (Math.PI / 180.0);
               float multiplier = this.isBaby() ? 0.1F : 0.2F;
               this.rollDelta = new Vec3(movement.x + -Mth.sin(angle) * multiplier, 0.0, movement.z + Mth.cos(angle) * multiplier);
               this.setDeltaMovement(this.rollDelta.add(0.0, 0.27, 0.0));
            } else if (this.rollCounter != 7.0F && this.rollCounter != 15.0F && this.rollCounter != 23.0F) {
               this.setDeltaMovement(this.rollDelta.x, movement.y, this.rollDelta.z);
            } else {
               this.setDeltaMovement(0.0, this.onGround() ? 0.27 : movement.y, 0.0);
            }
         }
      }
   }

   private void afterSneeze() {
      Vec3 movement = this.getDeltaMovement();
      Level level = this.level();
      level.addParticle(
         ParticleTypes.SNEEZE,
         this.getX() - (this.getBbWidth() + 1.0F) * 0.5 * Mth.sin(this.yBodyRot * (float) (Math.PI / 180.0)),
         this.getEyeY() - 0.1F,
         this.getZ() + (this.getBbWidth() + 1.0F) * 0.5 * Mth.cos(this.yBodyRot * (float) (Math.PI / 180.0)),
         movement.x,
         0.0,
         movement.z
      );
      this.playSound(SoundEvents.PANDA_SNEEZE, 1.0F, 1.0F);

      for (Panda panda : level.getEntitiesOfClass(Panda.class, this.getBoundingBox().inflate(10.0))) {
         if (!panda.isBaby() && panda.onGround() && !panda.isInWater() && panda.canPerformAction()) {
            panda.jumpFromGround();
         }
      }

      if (this.level() instanceof ServerLevel serverLevel && serverLevel.getGameRules().get(GameRules.MOB_DROPS)) {
         this.dropFromGiftLootTable(serverLevel, BuiltInLootTables.PANDA_SNEEZE, this::spawnAtLocation);
      }
   }

   @Override
   protected void pickUpItem(final ServerLevel level, final ItemEntity entity) {
      if (this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && canPickUpAndEat(entity)) {
         this.onItemPickup(entity);
         ItemStack itemStack = entity.getItem();
         this.setItemSlot(EquipmentSlot.MAINHAND, itemStack);
         this.setGuaranteedDrop(EquipmentSlot.MAINHAND);
         this.take(entity, itemStack.getCount());
         entity.discard();
      }
   }

   @Override
   public boolean hurtServer(final ServerLevel level, final DamageSource source, final float damage) {
      this.sit(false);
      return super.hurtServer(level, source, damage);
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(
      final ServerLevelAccessor level, final DifficultyInstance difficulty, final EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData
   ) {
      RandomSource random = level.getRandom();
      this.setMainGene(Panda$Gene.getRandom(random));
      this.setHiddenGene(Panda$Gene.getRandom(random));
      this.setAttributes();
      if (groupData == null) {
         groupData = new AgeableMob$AgeableMobGroupData(0.2F);
      }

      return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
   }

   public void setGeneFromParents(final Panda parent1, @Nullable final Panda parent2) {
      if (parent2 == null) {
         if (this.random.nextBoolean()) {
            this.setMainGene(parent1.getOneOfGenesRandomly());
            this.setHiddenGene(Panda$Gene.getRandom(this.random));
         } else {
            this.setMainGene(Panda$Gene.getRandom(this.random));
            this.setHiddenGene(parent1.getOneOfGenesRandomly());
         }
      } else if (this.random.nextBoolean()) {
         this.setMainGene(parent1.getOneOfGenesRandomly());
         this.setHiddenGene(parent2.getOneOfGenesRandomly());
      } else {
         this.setMainGene(parent2.getOneOfGenesRandomly());
         this.setHiddenGene(parent1.getOneOfGenesRandomly());
      }

      if (this.random.nextInt(32) == 0) {
         this.setMainGene(Panda$Gene.getRandom(this.random));
      }

      if (this.random.nextInt(32) == 0) {
         this.setHiddenGene(Panda$Gene.getRandom(this.random));
      }
   }

   private Panda$Gene getOneOfGenesRandomly() {
      return this.random.nextBoolean() ? this.getMainGene() : this.getHiddenGene();
   }

   public void setAttributes() {
      if (this.isWeak()) {
         this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(10.0);
      }

      if (this.isLazy()) {
         this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.07F);
      }
   }

   private void tryToSit() {
      if (!this.isInWater()) {
         this.setZza(0.0F);
         this.getNavigation().stop();
         this.sit(true);
      }
   }

   @Override
   public InteractionResult mobInteract(final Player player, final InteractionHand hand) {
      ItemStack interactionItemStack = player.getItemInHand(hand);
      if (this.isScared()) {
         return InteractionResult.PASS;
      } else if (this.isOnBack()) {
         this.setOnBack(false);
         return InteractionResult.SUCCESS;
      } else if (this.isFood(interactionItemStack)) {
         if (this.getTarget() != null) {
            this.gotBamboo = true;
         }

         if (this.canAgeUp()) {
            this.usePlayerItem(player, hand, interactionItemStack);
            this.ageUp((int)(-this.getAge() / 20 * 0.1F), true);
         } else {
            if (this.isBaby()) {
               return InteractionResult.PASS;
            }

            if (!this.level().isClientSide() && this.getAge() == 0 && this.canFallInLove()) {
               this.usePlayerItem(player, hand, interactionItemStack);
               this.setInLove(player);
            } else {
               if (!(this.level() instanceof ServerLevel level) || this.isSitting() || this.isInWater()) {
                  return InteractionResult.PASS;
               }

               this.tryToSit();
               this.eat(true);
               ItemStack pandasCurrentItem = this.getItemBySlot(EquipmentSlot.MAINHAND);
               if (!pandasCurrentItem.isEmpty() && !player.hasInfiniteMaterials()) {
                  this.spawnAtLocation(level, pandasCurrentItem);
               }

               this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(interactionItemStack.getItem(), 1));
               this.usePlayerItem(player, hand, interactionItemStack);
            }
         }

         return InteractionResult.SUCCESS_SERVER;
      } else {
         return (InteractionResult)(this.isBaby() && player.isHolding(Items.GOLDEN_DANDELION) ? super.mobInteract(player, hand) : InteractionResult.PASS);
      }
   }

   @Nullable
   @Override
   protected SoundEvent getAmbientSound() {
      if (this.isAggressive()) {
         return SoundEvents.PANDA_AGGRESSIVE_AMBIENT;
      } else {
         return this.isWorried() ? SoundEvents.PANDA_WORRIED_AMBIENT : SoundEvents.PANDA_AMBIENT;
      }
   }

   @Override
   protected void playStepSound(final BlockPos pos, final BlockState blockState) {
      this.playSound(SoundEvents.PANDA_STEP, 0.15F, 1.0F);
   }

   @Override
   public boolean isFood(final ItemStack itemStack) {
      return itemStack.is(ItemTags.PANDA_FOOD);
   }

   @Nullable
   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.PANDA_DEATH;
   }

   @Nullable
   @Override
   protected SoundEvent getHurtSound(final DamageSource source) {
      return SoundEvents.PANDA_HURT;
   }

   public boolean canPerformAction() {
      return !this.isOnBack() && !this.isScared() && !this.isEating() && !this.isRolling() && !this.isSitting();
   }

   @Override
   public EntityDimensions getDefaultDimensions(final Pose pose) {
      return this.isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
   }

   private static boolean canPickUpAndEat(final ItemEntity entity) {
      return entity.getItem().is(ItemTags.PANDA_EATS_FROM_GROUND) && entity.isAlive() && !entity.hasPickUpDelay();
   }
}
