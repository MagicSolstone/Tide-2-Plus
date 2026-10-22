package net.minecraft.world.entity.monster;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData$Builder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.BlockUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity$MovementEmission;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.InterpolationHandler;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.golem.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent$Context;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

public class Shulker extends AbstractGolem implements Enemy {
   private static final Identifier COVERED_ARMOR_MODIFIER_ID = Identifier.withDefaultNamespace("covered");
   private static final AttributeModifier COVERED_ARMOR_MODIFIER = new AttributeModifier(COVERED_ARMOR_MODIFIER_ID, 20.0, AttributeModifier$Operation.ADD_VALUE);
   protected static final EntityDataAccessor<Direction> DATA_ATTACH_FACE_ID = SynchedEntityData.defineId(Shulker.class, EntityDataSerializers.DIRECTION);
   protected static final EntityDataAccessor<Byte> DATA_PEEK_ID = SynchedEntityData.defineId(Shulker.class, EntityDataSerializers.BYTE);
   protected static final EntityDataAccessor<Byte> DATA_COLOR_ID = SynchedEntityData.defineId(Shulker.class, EntityDataSerializers.BYTE);
   private static final int TELEPORT_STEPS = 6;
   private static final byte NO_COLOR = 16;
   private static final byte DEFAULT_COLOR = 16;
   public static final int MAX_TELEPORT_DISTANCE = 8;
   private static final int OTHER_SHULKER_SCAN_RADIUS = 8;
   private static final int OTHER_SHULKER_LIMIT = 5;
   private static final float PEEK_PER_TICK = 0.05F;
   private static final byte DEFAULT_PEEK = 0;
   private static final Direction DEFAULT_ATTACH_FACE = Direction.DOWN;
   private static final Vector3fc FORWARD = Direction.SOUTH.getUnitVec3f();
   private static final float MAX_SCALE = 3.0F;
   private float currentPeekAmountO;
   private float currentPeekAmount;
   @Nullable
   private BlockPos clientOldAttachPosition;
   private int clientSideTeleportInterpolation;
   private static final float MAX_LID_OPEN = 1.0F;

   public Shulker(final EntityType<? extends Shulker> type, final Level level) {
      super(type, level);
      this.xpReward = 5;
      this.lookControl = new Shulker$ShulkerLookControl(this, this);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F, 0.02F, true));
      this.goalSelector.addGoal(4, new Shulker$ShulkerAttackGoal(this));
      this.goalSelector.addGoal(7, new Shulker$ShulkerPeekGoal(this));
      this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this, this.getClass()).setAlertOthers());
      this.targetSelector.addGoal(2, new Shulker$ShulkerNearestAttackGoal(this, this));
      this.targetSelector.addGoal(3, new Shulker$ShulkerDefenseAttackGoal(this));
   }

   @Override
   protected Entity$MovementEmission getMovementEmission() {
      return Entity$MovementEmission.NONE;
   }

   @Override
   public SoundSource getSoundSource() {
      return SoundSource.HOSTILE;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.SHULKER_AMBIENT;
   }

   @Override
   public void playAmbientSound() {
      if (!this.isClosed()) {
         super.playAmbientSound();
      }
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.SHULKER_DEATH;
   }

   @Override
   protected SoundEvent getHurtSound(final DamageSource source) {
      return this.isClosed() ? SoundEvents.SHULKER_HURT_CLOSED : SoundEvents.SHULKER_HURT;
   }

   @Override
   protected void defineSynchedData(final SynchedEntityData$Builder entityData) {
      super.defineSynchedData(entityData);
      entityData.define(DATA_ATTACH_FACE_ID, DEFAULT_ATTACH_FACE);
      entityData.define(DATA_PEEK_ID, (byte)0);
      entityData.define(DATA_COLOR_ID, (byte)16);
   }

   public static AttributeSupplier$Builder createAttributes() {
      return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0);
   }

   @Override
   protected BodyRotationControl createBodyControl() {
      return new Shulker$ShulkerBodyRotationControl(this);
   }

   @Override
   protected void readAdditionalSaveData(final ValueInput input) {
      super.readAdditionalSaveData(input);
      this.setAttachFace(input.<Direction>read("AttachFace", Direction.LEGACY_ID_CODEC).orElse(DEFAULT_ATTACH_FACE));
      this.entityData.set(DATA_PEEK_ID, input.getByteOr("Peek", (byte)0));
      this.entityData.set(DATA_COLOR_ID, input.getByteOr("Color", (byte)16));
   }

   @Override
   protected void addAdditionalSaveData(final ValueOutput output) {
      super.addAdditionalSaveData(output);
      output.store("AttachFace", Direction.LEGACY_ID_CODEC, this.getAttachFace());
      output.putByte("Peek", this.entityData.get(DATA_PEEK_ID));
      output.putByte("Color", this.entityData.get(DATA_COLOR_ID));
   }

   @Override
   public void tick() {
      super.tick();
      if (!this.level().isClientSide() && !this.isPassenger() && !this.canStayAt(this.blockPosition(), this.getAttachFace())) {
         this.findNewAttachment();
      }

      if (this.updatePeekAmount()) {
         this.onPeekAmountChange();
      }

      if (this.level().isClientSide()) {
         if (this.clientSideTeleportInterpolation > 0) {
            this.clientSideTeleportInterpolation--;
         } else {
            this.clientOldAttachPosition = null;
         }
      }
   }

   private void findNewAttachment() {
      Direction attachmentDirection = this.findAttachableSurface(this.blockPosition());
      if (attachmentDirection != null) {
         this.setAttachFace(attachmentDirection);
      } else {
         this.teleportSomewhere();
      }
   }

   @Override
   protected AABB makeBoundingBox(final Vec3 position) {
      float physPeek = getPhysicalPeek(this.currentPeekAmount);
      Direction direction = this.getAttachFace().getOpposite();
      return getProgressAabb(this.getScale(), direction, physPeek, position);
   }

   private static float getPhysicalPeek(final float amount) {
      return 0.5F - Mth.sin((0.5F + amount) * (float) Math.PI) * 0.5F;
   }

   @Override
   public EntityDimensions getDefaultDimensions(final Pose pose) {
      EntityDimensions dimension = super.getDefaultDimensions(pose);
      if (this.getAttachFace() == Direction.DOWN && this.currentPeekAmount > 0.0F) {
         float heightScaleFactor = 1.0F + this.currentPeekAmount;
         return dimension.scale(1.0F, heightScaleFactor);
      } else {
         return dimension;
      }
   }

   private boolean updatePeekAmount() {
      this.currentPeekAmountO = this.currentPeekAmount;
      float targetPeekAmount = this.getRawPeekAmount() * 0.01F;
      if (this.currentPeekAmount == targetPeekAmount) {
         return false;
      } else {
         if (this.currentPeekAmount > targetPeekAmount) {
            this.currentPeekAmount = Mth.clamp(this.currentPeekAmount - 0.05F, targetPeekAmount, 1.0F);
         } else {
            this.currentPeekAmount = Mth.clamp(this.currentPeekAmount + 0.05F, 0.0F, targetPeekAmount);
         }

         return true;
      }
   }

   private void onPeekAmountChange() {
      this.reapplyPosition();
      this.refreshDimensions();
      float physicalPeek = getPhysicalPeek(this.currentPeekAmount);
      float physicalPeekOld = getPhysicalPeek(this.currentPeekAmountO);
      Direction direction = this.getAttachFace().getOpposite();
      float push = (physicalPeek - physicalPeekOld) * this.getScale();
      if (!(push <= 0.0F)) {
         for (Entity entity : this.level()
            .getEntities(
               this,
               getProgressDeltaAabb(this.getScale(), direction, physicalPeekOld, physicalPeek, this.position()),
               EntitySelector.NO_SPECTATORS.and(e -> !e.isPassengerOfSameVehicle(this))
            )) {
            if (!(entity instanceof Shulker) && !entity.noPhysics) {
               entity.move(MoverType.SHULKER, new Vec3(push * direction.getStepX(), push * direction.getStepY(), push * direction.getStepZ()));
            }
         }
      }
   }

   public static AABB getProgressAabb(final float size, final Direction direction, final float progressTo, final Vec3 position) {
      return getProgressDeltaAabb(size, direction, -1.0F, progressTo, position);
   }

   public static AABB getProgressDeltaAabb(final float size, final Direction direction, final float progressFrom, final float progressTo, final Vec3 position) {
      AABB boundsAtBottomCenter = new AABB(-size * 0.5, 0.0, -size * 0.5, size * 0.5, size, size * 0.5);
      double maxMovement = Math.max(progressFrom, progressTo);
      double minMovement = Math.min(progressFrom, progressTo);
      AABB aabb = boundsAtBottomCenter.expandTowards(
            direction.getStepX() * maxMovement * size, direction.getStepY() * maxMovement * size, direction.getStepZ() * maxMovement * size
         )
         .contract(
            -direction.getStepX() * (1.0 + minMovement) * size,
            -direction.getStepY() * (1.0 + minMovement) * size,
            -direction.getStepZ() * (1.0 + minMovement) * size
         );
      return aabb.move(position.x, position.y, position.z);
   }

   @Override
   public boolean startRiding(final Entity entity, final boolean force, final boolean sendEventAndTriggers) {
      if (this.level().isClientSide()) {
         this.clientOldAttachPosition = null;
         this.clientSideTeleportInterpolation = 0;
      }

      this.setAttachFace(Direction.DOWN);
      return super.startRiding(entity, force, sendEventAndTriggers);
   }

   @Override
   public void stopRiding() {
      super.stopRiding();
      if (this.level().isClientSide()) {
         this.clientOldAttachPosition = this.blockPosition();
      }

      this.yBodyRotO = 0.0F;
      this.yBodyRot = 0.0F;
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(
      final ServerLevelAccessor level, final DifficultyInstance difficulty, final EntitySpawnReason spawnReason, @Nullable final SpawnGroupData groupData
   ) {
      this.setYRot(0.0F);
      this.yHeadRot = this.getYRot();
      this.setOldPosAndRot();
      return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
   }

   @Override
   public void move(final MoverType moverType, final Vec3 delta) {
      if (moverType == MoverType.SHULKER_BOX) {
         this.teleportSomewhere();
      } else {
         super.move(moverType, delta);
      }
   }

   @Override
   public Vec3 getDeltaMovement() {
      return Vec3.ZERO;
   }

   @Override
   public void setDeltaMovement(final Vec3 deltaMovement) {
   }

   @Override
   public void setPos(final double x, final double y, final double z) {
      BlockPos oldPos = this.blockPosition();
      if (this.isPassenger()) {
         super.setPos(x, y, z);
      } else {
         super.setPos(Mth.floor(x) + 0.5, Mth.floor(y + 0.5), Mth.floor(z) + 0.5);
      }

      if (this.tickCount != 0) {
         BlockPos pos = this.blockPosition();
         if (!pos.equals(oldPos)) {
            this.entityData.set(DATA_PEEK_ID, (byte)0);
            this.needsSync = true;
            if (this.level().isClientSide() && !this.isPassenger() && !pos.equals(this.clientOldAttachPosition)) {
               this.clientOldAttachPosition = oldPos;
               this.clientSideTeleportInterpolation = 6;
               this.xOld = this.getX();
               this.yOld = this.getY();
               this.zOld = this.getZ();
            }
         }
      }
   }

   @Nullable
   protected Direction findAttachableSurface(final BlockPos target) {
      for (Direction direction : Direction.values()) {
         if (this.canStayAt(target, direction)) {
            return direction;
         }
      }

      return null;
   }

   private boolean canStayAt(final BlockPos target, final Direction face) {
      if (this.isPositionBlocked(target)) {
         return false;
      } else {
         Direction oppositeFace = face.getOpposite();
         if (!this.level().loadedAndEntityCanStandOnFace(target.relative(face), this, oppositeFace)) {
            return false;
         } else {
            AABB fullyOpened = getProgressAabb(this.getScale(), oppositeFace, 1.0F, Vec3.atBottomCenterOf(target)).deflate(1.0E-6);
            return this.level().noCollision(this, fullyOpened);
         }
      }
   }

   private boolean isPositionBlocked(final BlockPos target) {
      BlockState state = this.level().getBlockState(target);
      if (state.isAir()) {
         return false;
      } else {
         boolean movingPistonInOurCurrentPosition = state.is(Blocks.MOVING_PISTON) && target.equals(this.blockPosition());
         return !movingPistonInOurCurrentPosition;
      }
   }

   protected boolean teleportSomewhere() {
      if (!this.isNoAi() && this.isAlive()) {
         BlockPos oldPos = this.blockPosition();
         Level level = this.level();

         for (int attempt = 0; attempt < 5; attempt++) {
            BlockPos target = oldPos.offset(
               Mth.randomBetweenInclusive(this.random, -8, 8), Mth.randomBetweenInclusive(this.random, -8, 8), Mth.randomBetweenInclusive(this.random, -8, 8)
            );
            if (target.getY() > level.getMinY()
               && level.isEmptyBlock(target)
               && level.getWorldBorder().isWithinBounds(target)
               && level.noCollision(this, new AABB(target).deflate(1.0E-6))) {
               Direction attachmentDirection = this.findAttachableSurface(target);
               if (attachmentDirection != null && !this.level().getBlockState(target.relative(attachmentDirection)).is(BlockTags.SHULKER_DOES_NOT_TELEPORT_TO)) {
                  this.unRide();
                  this.setAttachFace(attachmentDirection);
                  this.playSound(SoundEvents.SHULKER_TELEPORT, 1.0F, 1.0F);
                  this.setPos(target.getX() + 0.5, target.getY(), target.getZ() + 0.5);
                  level.gameEvent(GameEvent.TELEPORT, oldPos, GameEvent$Context.of(this));
                  int packedDiff = BlockUtil.packDifferenceInPosition(oldPos, this.blockPosition(), 8, 8, 8);
                  level.levelEvent(2016, oldPos, packedDiff);
                  this.entityData.set(DATA_PEEK_ID, (byte)0);
                  this.setTarget(null);
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   @Override
   protected InterpolationHandler createInterpolationHandler() {
      return InterpolationHandler.NO_OP;
   }

   @Override
   public boolean hurtServer(final ServerLevel level, final DamageSource source, final float damage) {
      if (this.isClosed()) {
         Entity directEntity = source.getDirectEntity();
         if (directEntity instanceof AbstractArrow) {
            return false;
         }
      }

      if (!super.hurtServer(level, source, damage)) {
         return false;
      } else {
         if (this.getHealth() < this.getMaxHealth() * 0.5 && this.random.nextInt(4) == 0) {
            this.teleportSomewhere();
         } else if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            Entity directEntity = source.getDirectEntity();
            if (directEntity != null && directEntity.is(EntityTypes.SHULKER_BULLET)) {
               this.hitByShulkerBullet();
            }
         }

         return true;
      }
   }

   private boolean isClosed() {
      return this.getRawPeekAmount() == 0;
   }

   private void hitByShulkerBullet() {
      Vec3 oldPosition = this.position();
      AABB oldAabb = this.getBoundingBox();
      if (!this.isClosed() && this.teleportSomewhere()) {
         int shulkerCount = this.level().getEntities(EntityTypes.SHULKER, oldAabb.inflate(8.0), Entity::isAlive).size();
         float failureChance = (shulkerCount - 1) / 5.0F;
         if (!(this.level().getRandom().nextFloat() < failureChance)) {
            Shulker baby = EntityTypes.SHULKER.create(this.level(), EntitySpawnReason.BREEDING);
            if (baby != null) {
               baby.setVariant(this.getVariant());
               baby.snapTo(oldPosition);
               this.level().addFreshEntity(baby);
            }
         }
      }
   }

   @Override
   public boolean canBeCollidedWith(@Nullable final Entity other) {
      return this.isAlive();
   }

   public Direction getAttachFace() {
      return this.entityData.get(DATA_ATTACH_FACE_ID);
   }

   private void setAttachFace(final Direction attachmentDirection) {
      this.entityData.set(DATA_ATTACH_FACE_ID, attachmentDirection);
   }

   @Override
   public void onSyncedDataUpdated(final EntityDataAccessor<?> accessor) {
      if (DATA_ATTACH_FACE_ID.equals(accessor)) {
         this.setBoundingBox(this.makeBoundingBox());
      }

      super.onSyncedDataUpdated(accessor);
   }

   private int getRawPeekAmount() {
      return this.entityData.get(DATA_PEEK_ID);
   }

   private void setRawPeekAmount(final int amount) {
      if (!this.level().isClientSide()) {
         this.getAttribute(Attributes.ARMOR).removeModifier(COVERED_ARMOR_MODIFIER_ID);
         if (amount == 0) {
            this.getAttribute(Attributes.ARMOR).addPermanentModifier(COVERED_ARMOR_MODIFIER);
            this.playSound(SoundEvents.SHULKER_CLOSE, 1.0F, 1.0F);
            this.gameEvent(GameEvent.CONTAINER_CLOSE);
         } else {
            this.playSound(SoundEvents.SHULKER_OPEN, 1.0F, 1.0F);
            this.gameEvent(GameEvent.CONTAINER_OPEN);
         }
      }

      this.entityData.set(DATA_PEEK_ID, (byte)amount);
   }

   public float getClientPeekAmount(final float a) {
      return Mth.lerp(a, this.currentPeekAmountO, this.currentPeekAmount);
   }

   @Override
   public void recreateFromPacket(final ClientboundAddEntityPacket packet) {
      super.recreateFromPacket(packet);
      this.yBodyRot = 0.0F;
      this.yBodyRotO = 0.0F;
   }

   @Override
   public int getMaxHeadXRot() {
      return 180;
   }

   @Override
   public int getMaxHeadYRot() {
      return 180;
   }

   @Override
   public void push(final Entity entity) {
   }

   @Nullable
   public Vec3 getRenderPosition(final float a) {
      if (this.clientOldAttachPosition != null && this.clientSideTeleportInterpolation > 0) {
         double scale = (this.clientSideTeleportInterpolation - a) / 6.0;
         scale *= scale;
         scale *= this.getScale();
         BlockPos currentPos = this.blockPosition();
         double ox = (currentPos.getX() - this.clientOldAttachPosition.getX()) * scale;
         double oy = (currentPos.getY() - this.clientOldAttachPosition.getY()) * scale;
         double oz = (currentPos.getZ() - this.clientOldAttachPosition.getZ()) * scale;
         return new Vec3(-ox, -oy, -oz);
      } else {
         return null;
      }
   }

   @Override
   protected float sanitizeScale(final float scale) {
      return Math.min(scale, 3.0F);
   }

   private void setVariant(final Optional<DyeColor> color) {
      this.entityData.set(DATA_COLOR_ID, color.<Byte>map(dyeColor -> (byte)dyeColor.getId()).orElse((byte)16));
   }

   public Optional<DyeColor> getVariant() {
      return Optional.ofNullable(this.getColor());
   }

   @Nullable
   public DyeColor getColor() {
      byte color = this.entityData.get(DATA_COLOR_ID);
      return color != 16 && color <= 15 ? DyeColor.byId(color) : null;
   }

   @Nullable
   @Override
   public <T> T get(final DataComponentType<? extends T> type) {
      return type == DataComponents.SHULKER_COLOR ? castComponentValue((DataComponentType<T>)type, this.getColor()) : super.get(type);
   }

   @Override
   protected void applyImplicitComponents(final DataComponentGetter components) {
      this.applyImplicitComponentIfPresent(components, DataComponents.SHULKER_COLOR);
      super.applyImplicitComponents(components);
   }

   @Override
   protected <T> boolean applyImplicitComponent(final DataComponentType<T> type, final T value) {
      if (type == DataComponents.SHULKER_COLOR) {
         this.setVariant(Optional.of(castComponentValue(DataComponents.SHULKER_COLOR, value)));
         return true;
      } else {
         return super.applyImplicitComponent(type, value);
      }
   }
}
