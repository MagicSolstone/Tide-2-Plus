package net.minecraft.world.entity.decoration;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData$Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Continuation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class Cushion extends BlockAttachedEntity {
   private static final DyeColor DEFAULT_COLOR = DyeColor.WHITE;
   private static final int LIGHTNING_DROP_INVULNERABLE_TICKS = 20;
   private static final EntityDataAccessor<DyeColor> DATA_COLOR = SynchedEntityData.defineId(Cushion.class, EntityDataSerializers.DYE_COLOR);

   public Cushion(final EntityType<Cushion> type, final Level level) {
      super(type, level);
   }

   public DyeColor getColor() {
      return this.entityData.get(DATA_COLOR);
   }

   public void setColor(final DyeColor color) {
      this.entityData.set(DATA_COLOR, color);
   }

   @Override
   public void dropItem(final ServerLevel level, @Nullable final Entity causedBy) {
      this.playSound(SoundEvents.CUSHION_BREAK, 1.0F, 1.0F);
      this.showBreakingParticles();
      if (level.getGameRules().get(GameRules.ENTITY_DROPS)) {
         if (!(causedBy instanceof Player player && player.hasInfiniteMaterials())) {
            ItemEntity itemEntity = this.spawnAtLocation(level, this.getCushionItemStackWithData());
            if (itemEntity != null && causedBy instanceof LightningBolt) {
               itemEntity.setInvulnerableTime(20);
            }
         }
      }
   }

   private static boolean isBreakingDeniedFor(final DamageSource source) {
      return source.getEntity() instanceof Player player && !player.mayBuild();
   }

   private boolean isBreakingDeniedAtPosFor(final ServerLevel level, final DamageSource source) {
      return source.getEntity() instanceof Player player && !level.mayInteract(player, this.pos);
   }

   @Override
   public boolean hurtServer(final ServerLevel level, final DamageSource source, final float damage) {
      return !isBreakingDeniedFor(source) && !this.isBreakingDeniedAtPosFor(level, source) ? super.hurtServer(level, source, damage) : false;
   }

   @Override
   public boolean hurtClient(final DamageSource source) {
      return isBreakingDeniedFor(source) ? false : super.hurtClient(source);
   }

   @Override
   public InteractionResult interact(final Player player, final InteractionHand hand, final Vec3 location) {
      if (player.isSecondaryUseActive() || this.isVehicle()) {
         return InteractionResult.PASS;
      } else if (!this.level().isClientSide() && player.startRiding(this)) {
         this.playSound(SoundEvents.CUSHION_SIT, 1.0F, 1.0F);
         return InteractionResult.SUCCESS_SERVER;
      } else {
         return InteractionResult.CONSUME;
      }
   }

   @Override
   protected void removePassenger(final Entity passenger) {
      super.removePassenger(passenger);
      if (!this.level().isClientSide() && this.getRemovalReason() == null) {
         this.playSound(SoundEvents.CUSHION_GET_UP, 1.0F, 1.0F);
      }
   }

   @Override
   public ItemStack getPickResult() {
      return new ItemStack(Items.CUSHION.pick(this.getColor()));
   }

   @Override
   protected void tickAtCheckInterval() {
      if (this.level() instanceof ServerLevel level) {
         BlockPos blockPos = this.blockPosition();
         FluidState fluidState = level.getBlockState(blockPos).getFluidState();
         if (this.collidedWithFluid(fluidState, blockPos, this.position(), this.position())) {
            fluidState.entityInside(level, blockPos, this, this.insideEffectCollector);
            this.insideEffectCollector.applyAndClear(this);
         }

         this.destroyIfInFire(level);
      }
   }

   public void destroyIfInFire(final ServerLevel level) {
      if (!this.isRemoved()) {
         level.findBlocksIn(this.getBoundingBox().nextDeflated()).filterState(state -> state.is(BlockTags.FIRE)).forEachUntil((var2, var3) -> {
            this.hurtServer(level, this.damageSources().inFire(), 1.0F);
            return Continuation.ABORT;
         });
      }
   }

   @Override
   public void thunderHit(final ServerLevel level, final LightningBolt lightningBolt) {
      if (!this.isRemoved()) {
         this.kill(level, lightningBolt);
         this.dropItem(level, lightningBolt);
      }
   }

   @Override
   public void setPos(final double x, final double y, final double z) {
      this.setPosRaw(x, y, z);
      super.setPos(x, y, z);
   }

   private void showBreakingParticles() {
      if (this.level() instanceof ServerLevel level) {
         level.sendParticles(
            new BlockParticleOption(ParticleTypes.BLOCK, Blocks.WOOL.pick(this.getColor()).defaultBlockState()),
            this.getX(),
            this.getY(0.6666666666666666),
            this.getZ(),
            10,
            this.getBbWidth() / 4.0F,
            this.getBbHeight() / 4.0F,
            this.getBbWidth() / 4.0F,
            0.05
         );
      }
   }

   public static boolean canBePlacedAt(final Level level, final AABB boundingBox) {
      return wouldSurviveAt(level, boundingBox) && !isAnchorBuried(level, boundingBox);
   }

   public static boolean wouldSurviveAt(final Level level, final AABB boundingBox) {
      return hasAnchorBelow(level, boundingBox) && !isCoveredBySuffocatingBlocks(level, boundingBox);
   }

   private static boolean hasAnchorBelow(final Level level, final AABB boundingBox) {
      AABB anchorBox = new AABB(
         boundingBox.minX, boundingBox.minY - 0.015625, boundingBox.minZ, Math.nextDown(boundingBox.maxX), boundingBox.minY, Math.nextDown(boundingBox.maxZ)
      );
      return level.findBlocksIn(anchorBox.expandTowards(0.0, -0.125, 0.0)).forEachUntil((blockPos, blockState) -> {
         VoxelShape shape = blockState.getShape(level, blockPos);
         return !shape.isEmpty() && shape.bounds().move(blockPos).intersects(anchorBox) ? Continuation.ABORT : Continuation.CONTINUE;
      });
   }

   private static boolean isAnchorBuried(final Level level, final AABB boundingBox) {
      AABB restingSlice = new AABB(boundingBox.minX, boundingBox.minY, boundingBox.minZ, boundingBox.maxX, boundingBox.minY + 0.015625, boundingBox.maxZ)
         .nextDeflated();
      VoxelShape exposedSurface = Shapes.create(restingSlice);

      for (VoxelShape collider : level.getBlockCollisions(null, restingSlice)) {
         exposedSurface = Shapes.join(exposedSurface, collider, BooleanOp.ONLY_FIRST);
         if (exposedSurface.isEmpty()) {
            return true;
         }
      }

      return false;
   }

   private static boolean isCoveredBySuffocatingBlocks(final Level level, final AABB boundingBox) {
      for (BlockPos blockPos : BlockPos.betweenClosed(boundingBox.nextDeflated())) {
         if (!level.getBlockState(blockPos).isSuffocating(level, blockPos)) {
            return false;
         }
      }

      return true;
   }

   @Override
   public boolean survives() {
      return wouldSurviveAt(this.level(), this.getBoundingBox());
   }

   @Override
   protected void recalculateBoundingBox() {
      this.setBoundingBox(this.makeBoundingBox());
   }

   @Override
   protected void defineSynchedData(final SynchedEntityData$Builder entityData) {
      entityData.define(DATA_COLOR, DEFAULT_COLOR);
   }

   @Override
   protected void addAdditionalSaveData(final ValueOutput output) {
      super.addAdditionalSaveData(output);
      output.store("color", DyeColor.CODEC, this.getColor());
   }

   @Override
   protected void readAdditionalSaveData(final ValueInput input) {
      super.readAdditionalSaveData(input);
      this.setColor(input.<DyeColor>read("color", DyeColor.CODEC).orElse(DEFAULT_COLOR));
   }

   @Nullable
   @Override
   public <T> T get(final DataComponentType<? extends T> type) {
      return type == DataComponents.CUSHION_COLOR ? castComponentValue((DataComponentType<T>)type, this.getColor()) : super.get(type);
   }

   @Override
   protected void applyImplicitComponents(final DataComponentGetter components) {
      this.applyImplicitComponentIfPresent(components, DataComponents.CUSHION_COLOR);
      super.applyImplicitComponents(components);
   }

   @Override
   protected <T> boolean applyImplicitComponent(final DataComponentType<T> type, final T value) {
      if (type == DataComponents.CUSHION_COLOR) {
         this.setColor(castComponentValue(DataComponents.CUSHION_COLOR, value));
         return true;
      } else {
         return super.applyImplicitComponent(type, value);
      }
   }

   private ItemStack getCushionItemStackWithData() {
      ItemStack itemStack = new ItemStack(Items.CUSHION.pick(this.getColor()));
      itemStack.set(DataComponents.CUSTOM_NAME, this.getCustomName());
      return itemStack;
   }
}
