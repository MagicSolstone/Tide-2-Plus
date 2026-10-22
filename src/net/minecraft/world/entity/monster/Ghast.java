package net.minecraft.world.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData$Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class Ghast extends Mob implements Enemy {
   private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING = SynchedEntityData.defineId(Ghast.class, EntityDataSerializers.BOOLEAN);
   private static final byte DEFAULT_EXPLOSION_POWER = 1;
   private int explosionPower = 1;

   public Ghast(final EntityType<? extends Ghast> type, final Level level) {
      super(type, level);
      this.xpReward = 5;
      this.moveControl = new Ghast$GhastMoveControl<>(this, false, () -> false);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(5, new Ghast$RandomFloatAroundGoal(this));
      this.goalSelector.addGoal(7, new Ghast$GhastLookGoal(this));
      this.goalSelector.addGoal(7, new Ghast$GhastShootFireballGoal(this));
      this.targetSelector
         .addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (target, level) -> Math.abs(target.getY() - this.getY()) <= 4.0));
   }

   public boolean isCharging() {
      return this.entityData.get(DATA_IS_CHARGING);
   }

   public void setCharging(final boolean onOff) {
      this.entityData.set(DATA_IS_CHARGING, onOff);
   }

   public int getExplosionPower() {
      return this.explosionPower;
   }

   private static boolean isReflectedFireball(final DamageSource source) {
      return source.getDirectEntity() instanceof LargeFireball && source.getEntity() instanceof Player;
   }

   @Override
   public boolean isInvulnerableTo(final ServerLevel level, final DamageSource source) {
      return this.isInvulnerable() && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)
         || !isReflectedFireball(source) && super.isInvulnerableTo(level, source);
   }

   @Override
   protected void checkFallDamage(final double ya, final boolean onGround, final BlockState onState, final BlockPos pos) {
   }

   @Override
   public boolean onClimbable() {
      return false;
   }

   @Override
   public void travel(final Vec3 input) {
      this.travelFlying(input, 0.02F);
   }

   @Override
   public boolean hurtServer(final ServerLevel level, final DamageSource source, final float damage) {
      if (isReflectedFireball(source)) {
         super.hurtServer(level, source, 1000.0F);
         return true;
      } else {
         return this.isInvulnerableTo(level, source) ? false : super.hurtServer(level, source, damage);
      }
   }

   @Override
   protected void defineSynchedData(final SynchedEntityData$Builder entityData) {
      super.defineSynchedData(entityData);
      entityData.define(DATA_IS_CHARGING, false);
   }

   public static AttributeSupplier$Builder createAttributes() {
      return Mob.createMobAttributes()
         .add(Attributes.MAX_HEALTH, 10.0)
         .add(Attributes.FOLLOW_RANGE, 100.0)
         .add(Attributes.CAMERA_DISTANCE, 8.0)
         .add(Attributes.FLYING_SPEED, 0.06);
   }

   @Override
   public SoundSource getSoundSource() {
      return SoundSource.HOSTILE;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.GHAST_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(final DamageSource source) {
      return SoundEvents.GHAST_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.GHAST_DEATH;
   }

   @Override
   protected float getSoundVolume() {
      return 5.0F;
   }

   public static boolean checkGhastSpawnRules(
      final EntityType<Ghast> type, final LevelAccessor level, final EntitySpawnReason spawnReason, final BlockPos pos, final RandomSource random
   ) {
      return level.getDifficulty() != Difficulty.PEACEFUL && random.nextInt(20) == 0 && checkMobSpawnRules(type, level, spawnReason, pos, random);
   }

   @Override
   public int getMaxSpawnClusterSize() {
      return 1;
   }

   @Override
   protected void addAdditionalSaveData(final ValueOutput output) {
      super.addAdditionalSaveData(output);
      output.putByte("ExplosionPower", (byte)this.explosionPower);
   }

   @Override
   protected void readAdditionalSaveData(final ValueInput input) {
      super.readAdditionalSaveData(input);
      this.explosionPower = input.getByteOr("ExplosionPower", (byte)1);
   }

   @Override
   public boolean supportQuadLeashAsHolder() {
      return true;
   }

   @Override
   public double leashElasticDistance() {
      return 10.0;
   }

   @Override
   public double leashSnapDistance() {
      return 16.0;
   }

   public static void faceMovementDirection(final Mob ghast) {
      if (ghast.getTarget() == null) {
         Vec3 movement = ghast.getDeltaMovement();
         ghast.setYRot(-((float)Mth.atan2(movement.x, movement.z)) * (180.0F / (float)Math.PI));
         ghast.yBodyRot = ghast.getYRot();
      } else {
         LivingEntity target = ghast.getTarget();
         double maxDist = 64.0;
         if (target.distanceToSqr(ghast) < 4096.0) {
            double xdd = target.getX() - ghast.getX();
            double zdd = target.getZ() - ghast.getZ();
            ghast.setYRot(-((float)Mth.atan2(xdd, zdd)) * (180.0F / (float)Math.PI));
            ghast.yBodyRot = ghast.getYRot();
         }
      }
   }
}
