package net.minecraft.world.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData$Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class Phantom extends Mob implements Enemy {
   public static final float FLAP_DEGREES_PER_TICK = 7.448451F;
   public static final int TICKS_PER_FLAP = Mth.ceil(24.166098F);
   private static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(Phantom.class, EntityDataSerializers.INT);
   private Vec3 moveTargetPoint = Vec3.ZERO;
   @Nullable
   private BlockPos anchorPoint;
   private Phantom$AttackPhase attackPhase = Phantom$AttackPhase.CIRCLE;

   public Phantom(final EntityType<? extends Phantom> type, final Level level) {
      super(type, level);
      this.xpReward = 5;
      this.moveControl = new Phantom$PhantomMoveControl<>(this, this);
      this.lookControl = new Phantom$PhantomLookControl(this);
   }

   @Override
   public boolean isFlapping() {
      return (this.getUniqueFlapTickOffset() + this.tickCount) % TICKS_PER_FLAP == 0;
   }

   @Override
   protected BodyRotationControl createBodyControl() {
      return new Phantom$PhantomBodyRotationControl(this, this);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(1, new Phantom$PhantomAttackStrategyGoal(this));
      this.goalSelector.addGoal(2, new Phantom$PhantomSweepAttackGoal(this));
      this.goalSelector.addGoal(3, new Phantom$PhantomCircleAroundAnchorGoal(this));
      this.targetSelector.addGoal(1, new Phantom$PhantomAttackPlayerTargetGoal(this));
   }

   @Override
   protected void defineSynchedData(final SynchedEntityData$Builder entityData) {
      super.defineSynchedData(entityData);
      entityData.define(ID_SIZE, 0);
   }

   public void setPhantomSize(final int size) {
      this.entityData.set(ID_SIZE, Mth.clamp(size, 0, 64));
   }

   private void updatePhantomSizeInfo() {
      this.refreshDimensions();
      this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(6 + this.getPhantomSize());
   }

   public int getPhantomSize() {
      return this.entityData.get(ID_SIZE);
   }

   @Override
   public void onSyncedDataUpdated(final EntityDataAccessor<?> accessor) {
      if (ID_SIZE.equals(accessor)) {
         this.updatePhantomSizeInfo();
      }

      super.onSyncedDataUpdated(accessor);
   }

   public int getUniqueFlapTickOffset() {
      return this.getId() * 3;
   }

   @Override
   public void tick() {
      super.tick();
      if (this.level().isClientSide()) {
         float anim = Mth.cos((this.getUniqueFlapTickOffset() + this.tickCount) * 7.448451F * (float) (Math.PI / 180.0) + (float) Math.PI);
         float nextAnim = Mth.cos((this.getUniqueFlapTickOffset() + this.tickCount + 1) * 7.448451F * (float) (Math.PI / 180.0) + (float) Math.PI);
         if (anim > 0.0F && nextAnim <= 0.0F) {
            this.level()
               .playLocalSound(
                  this, SoundEvents.PHANTOM_FLAP, this.getSoundSource(), 0.95F + this.random.nextFloat() * 0.05F, 0.95F + this.random.nextFloat() * 0.05F
               );
         }

         float width = this.getBbWidth() * 1.48F;
         float c = Mth.cos(this.getYRot() * (float) (Math.PI / 180.0)) * width;
         float s = Mth.sin(this.getYRot() * (float) (Math.PI / 180.0)) * width;
         float h = (0.3F + anim * 0.45F) * this.getBbHeight() * 2.5F;
         this.level().addParticle(ParticleTypes.MYCELIUM, this.getX() + c, this.getY() + h, this.getZ() + s, 0.0, 0.0, 0.0);
         this.level().addParticle(ParticleTypes.MYCELIUM, this.getX() - c, this.getY() + h, this.getZ() - s, 0.0, 0.0, 0.0);
      }
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
      this.travelFlying(input, 0.2F);
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(
      final ServerLevelAccessor level, final DifficultyInstance difficulty, final EntitySpawnReason spawnReason, @Nullable final SpawnGroupData groupData
   ) {
      this.anchorPoint = this.blockPosition().above(5);
      this.setPhantomSize(0);
      return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
   }

   @Override
   protected void readAdditionalSaveData(final ValueInput input) {
      super.readAdditionalSaveData(input);
      this.anchorPoint = input.<BlockPos>read("anchor_pos", BlockPos.CODEC).orElse(null);
      this.setPhantomSize(input.getIntOr("size", 0));
   }

   @Override
   protected void addAdditionalSaveData(final ValueOutput output) {
      super.addAdditionalSaveData(output);
      output.storeNullable("anchor_pos", BlockPos.CODEC, this.anchorPoint);
      output.putInt("size", this.getPhantomSize());
   }

   @Override
   public boolean shouldRenderAtSqrDistance(final double distance) {
      return true;
   }

   @Override
   public SoundSource getSoundSource() {
      return SoundSource.HOSTILE;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.PHANTOM_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(final DamageSource source) {
      return SoundEvents.PHANTOM_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.PHANTOM_DEATH;
   }

   @Override
   public EntityDimensions getDefaultDimensions(final Pose pose) {
      int size = this.getPhantomSize();
      EntityDimensions originalDimensions = super.getDefaultDimensions(pose);
      return originalDimensions.scale(1.0F + 0.15F * size);
   }

   private boolean canAttack(final ServerLevel level, final LivingEntity target, final TargetingConditions targetConditions) {
      return targetConditions.test(level, this, target);
   }
}
