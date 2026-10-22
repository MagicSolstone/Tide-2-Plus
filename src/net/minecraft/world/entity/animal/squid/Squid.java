package net.minecraft.world.entity.animal.squid;

import java.util.Objects;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AgeableMob$AgeableMobGroupData;
import net.minecraft.world.entity.Entity$MovementEmission;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityEvent$Value;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AgeableWaterCreature;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class Squid extends AgeableWaterCreature {
   public float xBodyRot;
   public float xBodyRotO;
   public float zBodyRot;
   public float zBodyRotO;
   public float tentacleMovement;
   public float oldTentacleMovement;
   public float tentacleAngle;
   public float oldTentacleAngle;
   private float speed;
   private float tentacleSpeed;
   private float rotateSpeed;
   private Vec3 movementVector = Vec3.ZERO;
   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.5F, 0.5F).withEyeHeight(0.37F);

   public Squid(final EntityType<? extends Squid> type, final Level level) {
      super(type, level);
      this.tentacleSpeed = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(0, new Squid$SquidRandomMovementGoal(this));
      this.goalSelector.addGoal(1, new Squid$SquidFleeGoal(this));
   }

   public static AttributeSupplier$Builder createAttributes() {
      return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0);
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.SQUID_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(final DamageSource source) {
      return SoundEvents.SQUID_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.SQUID_DEATH;
   }

   protected SoundEvent getSquirtSound() {
      return SoundEvents.SQUID_SQUIRT;
   }

   @Override
   public boolean canBeLeashed() {
      return true;
   }

   @Override
   protected float getSoundVolume() {
      return 0.4F;
   }

   @Override
   protected Entity$MovementEmission getMovementEmission() {
      return Entity$MovementEmission.EVENTS;
   }

   @Nullable
   @Override
   public AgeableMob getBreedOffspring(final ServerLevel level, final AgeableMob partner) {
      return EntityTypes.SQUID.create(level, EntitySpawnReason.BREEDING);
   }

   @Override
   protected double getDefaultGravity() {
      return 0.08;
   }

   @Override
   public void aiStep() {
      super.aiStep();
      this.xBodyRotO = this.xBodyRot;
      this.zBodyRotO = this.zBodyRot;
      this.oldTentacleMovement = this.tentacleMovement;
      this.oldTentacleAngle = this.tentacleAngle;
      this.tentacleMovement = this.tentacleMovement + this.tentacleSpeed;
      if (this.tentacleMovement > Math.PI * 2) {
         if (this.level().isClientSide()) {
            this.tentacleMovement = (float) (Math.PI * 2);
         } else {
            this.tentacleMovement -= (float) (Math.PI * 2);
            if (this.random.nextInt(10) == 0) {
               this.tentacleSpeed = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
            }

            this.level().broadcastEntityEvent(this, (byte)19);
         }
      }

      if (this.isInWater()) {
         if (this.tentacleMovement < (float) Math.PI) {
            float tentacleScale = this.tentacleMovement / (float) Math.PI;
            this.tentacleAngle = Mth.sin(tentacleScale * tentacleScale * (float) Math.PI) * (float) Math.PI * 0.25F;
            if (tentacleScale > 0.75) {
               if (this.isLocalInstanceAuthoritative()) {
                  this.setDeltaMovement(this.movementVector);
               }

               this.rotateSpeed = 1.0F;
            } else {
               this.rotateSpeed *= 0.8F;
            }
         } else {
            this.tentacleAngle = 0.0F;
            if (this.isLocalInstanceAuthoritative()) {
               this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            }

            this.rotateSpeed *= 0.99F;
         }

         Vec3 movement = this.getDeltaMovement();
         double horizontalMovement = movement.horizontalDistance();
         this.yBodyRot = this.yBodyRot + (-((float)Mth.atan2(movement.x, movement.z)) * (180.0F / (float)Math.PI) - this.yBodyRot) * 0.1F;
         this.setYRot(this.yBodyRot);
         this.zBodyRot = this.zBodyRot + (float) Math.PI * this.rotateSpeed * 1.5F;
         this.xBodyRot = this.xBodyRot + (-((float)Mth.atan2(horizontalMovement, movement.y)) * (180.0F / (float)Math.PI) - this.xBodyRot) * 0.1F;
      } else {
         this.tentacleAngle = Mth.abs(Mth.sin(this.tentacleMovement)) * (float) Math.PI * 0.25F;
         if (!this.level().isClientSide()) {
            double yd = this.getDeltaMovement().y;
            if (this.hasEffect(MobEffects.LEVITATION)) {
               yd = 0.05 * (this.getEffect(MobEffects.LEVITATION).getAmplifier() + 1);
            } else {
               yd -= this.getGravity();
            }

            this.setDeltaMovement(0.0, yd * this.getAirDrag(), 0.0);
         }

         this.xBodyRot = this.xBodyRot + (-90.0F - this.xBodyRot) * 0.02F;
      }
   }

   @Override
   public boolean hurtServer(final ServerLevel level, final DamageSource source, final float damage) {
      if (super.hurtServer(level, source, damage) && this.getLastHurtByMob() != null) {
         this.spawnInk();
         return true;
      } else {
         return false;
      }
   }

   private Vec3 rotateVector(final Vec3 vec) {
      Vec3 v = vec.xRot(this.xBodyRotO * (float) (Math.PI / 180.0));
      return v.yRot(-this.yBodyRotO * (float) (Math.PI / 180.0));
   }

   private void spawnInk() {
      this.makeSound(this.getSquirtSound());
      Vec3 pos = this.rotateVector(new Vec3(0.0, -1.0, 0.0)).add(this.getX(), this.getY(), this.getZ());

      for (int i = 0; i < 30; i++) {
         Vec3 dir = this.rotateVector(new Vec3(this.random.nextFloat() * 0.6 - 0.3, -1.0, this.random.nextFloat() * 0.6 - 0.3));
         float inkPosOffsetScale = this.isBaby() ? 0.1F : 0.3F;
         Vec3 dirOffset = dir.scale(inkPosOffsetScale + this.random.nextFloat() * 2.0F);
         ((ServerLevel)this.level()).sendParticles(this.getInkParticle(), pos.x, pos.y + 0.5, pos.z, 0, dirOffset.x, dirOffset.y, dirOffset.z, 0.1F);
      }
   }

   protected ParticleOptions getInkParticle() {
      return ParticleTypes.SQUID_INK;
   }

   @Override
   public void travel(final Vec3 input) {
      this.move(MoverType.SELF, this.getDeltaMovement());
   }

   @Override
   public void handleEntityEvent(@EntityEvent$Value final byte id) {
      if (id == 19) {
         this.tentacleMovement = 0.0F;
      } else {
         super.handleEntityEvent(id);
      }
   }

   public boolean hasMovementVector() {
      return this.movementVector.lengthSqr() > 1.0E-5F;
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(
      final ServerLevelAccessor level, final DifficultyInstance difficulty, final EntitySpawnReason spawnReason, @Nullable final SpawnGroupData groupData
   ) {
      SpawnGroupData spawnGroupData = Objects.requireNonNullElseGet(groupData, () -> new AgeableMob$AgeableMobGroupData(0.05F));
      return super.finalizeSpawn(level, difficulty, spawnReason, spawnGroupData);
   }

   @Override
   public EntityDimensions getDefaultDimensions(final Pose pose) {
      return this.isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
   }
}
