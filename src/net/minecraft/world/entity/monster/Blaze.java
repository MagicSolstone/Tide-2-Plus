package net.minecraft.world.entity.monster;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData$Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;

public class Blaze extends Monster {
   private float allowedHeightOffset = 0.5F;
   private int nextHeightOffsetChangeTick;
   private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Blaze.class, EntityDataSerializers.BYTE);

   public Blaze(final EntityType<? extends Blaze> blaze, final Level level) {
      super(blaze, level);
      this.setPathfindingMalus(PathType.WATER, -1.0F);
      this.setPathfindingMalus(PathType.LAVA, 8.0F);
      this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, 0.0F);
      this.setPathfindingMalus(PathType.FIRE, 0.0F);
      this.xpReward = 10;
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(4, new Blaze$BlazeAttackGoal(this));
      this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0));
      this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0, 0.0F));
      this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
   }

   public static AttributeSupplier$Builder createAttributes() {
      return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 6.0).add(Attributes.MOVEMENT_SPEED, 0.23F).add(Attributes.FOLLOW_RANGE, 48.0);
   }

   @Override
   protected void defineSynchedData(final SynchedEntityData$Builder entityData) {
      super.defineSynchedData(entityData);
      entityData.define(DATA_FLAGS_ID, (byte)0);
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.BLAZE_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(final DamageSource source) {
      return SoundEvents.BLAZE_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.BLAZE_DEATH;
   }

   @Override
   public float getLightLevelDependentMagicValue() {
      return 1.0F;
   }

   @Override
   public void aiStep() {
      if (!this.onGround() && this.getDeltaMovement().y < 0.0) {
         this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 0.6, 1.0));
      }

      if (this.level().isClientSide()) {
         if (this.random.nextInt(24) == 0 && !this.isSilent()) {
            this.level()
               .playLocalSound(
                  this.getX() + 0.5,
                  this.getY() + 0.5,
                  this.getZ() + 0.5,
                  SoundEvents.BLAZE_BURN,
                  this.getSoundSource(),
                  1.0F + this.random.nextFloat(),
                  this.random.nextFloat() * 0.7F + 0.3F,
                  false
               );
         }

         for (int i = 0; i < 2; i++) {
            this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), 0.0, 0.0, 0.0);
         }
      }

      super.aiStep();
   }

   @Override
   public boolean isSensitiveToWater() {
      return true;
   }

   @Override
   protected void customServerAiStep(final ServerLevel level) {
      this.nextHeightOffsetChangeTick--;
      if (this.nextHeightOffsetChangeTick <= 0) {
         this.nextHeightOffsetChangeTick = 100;
         this.allowedHeightOffset = (float)this.random.triangle(0.5, 6.891);
      }

      LivingEntity target = this.getTarget();
      if (target != null && target.getEyeY() > this.getEyeY() + this.allowedHeightOffset && this.canAttack(target)) {
         Vec3 movement = this.getDeltaMovement();
         this.setDeltaMovement(this.getDeltaMovement().add(0.0, (0.3F - movement.y) * 0.3F, 0.0));
         this.needsSync = true;
      }

      super.customServerAiStep(level);
   }

   @Override
   public boolean isOnFire() {
      return this.isCharged();
   }

   private boolean isCharged() {
      return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
   }

   private void setCharged(final boolean value) {
      byte flags = this.entityData.get(DATA_FLAGS_ID);
      if (value) {
         flags = (byte)(flags | 1);
      } else {
         flags = (byte)(flags & -2);
      }

      this.entityData.set(DATA_FLAGS_ID, flags);
   }
}
