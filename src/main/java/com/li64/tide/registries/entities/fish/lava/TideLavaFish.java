package com.li64.tide.registries.entities.fish.lava;

import com.li64.tide.data.FishLengthHolder;
import com.li64.tide.data.TideTags;
import com.li64.tide.data.fishing.FishData;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

import java.util.Optional;

public class TideLavaFish extends LavaAnimal implements Bucketable, FishLengthHolder {
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(TideLavaFish.class, EntityDataSerializers.BOOLEAN);

    private final Item bucketItem;
    private double length;

    public TideLavaFish(EntityType<? extends LavaAnimal> entityType, Level level) {
        super(entityType, level);
        ResourceLocation key = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
        Item fishItem = BuiltInRegistries.ITEM.getOptional(key).orElseThrow();
        this.bucketItem = BuiltInRegistries.ITEM.getOptional(key.withSuffix("_bucket")).orElseThrow();
        this.length = FishData.get(fishItem).map(data -> data.getRandomLength(getRandom())).orElse(0.0);
        this.moveControl = new LavaFishMoveControl(this);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(4, new LavaFishSwimGoal(this));
    }

    @Override
    public void travel(@NotNull Vec3 movement) {
        if (this.isEffectiveAi() && this.isInLava()) {
            this.moveRelative(this.getSpeed() / 50f, movement);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
        }
        else super.travel(movement);
    }


    @Override
    public void aiStep() {
        if (this.isInWater()) this.hurt(damageSources().freeze(), 4.0f);
        if (!this.isInLiquid() && this.onGround() && this.verticalCollision) {
            this.setDeltaMovement(this.getDeltaMovement().add(
                    (this.random.nextFloat() * 2.0 - 1.0) * 0.05, 0.4,
                    (this.random.nextFloat() * 2.0 - 1.0) * 0.05)
            );
            this.setOnGround(false);
            this.hasImpulse = true;
            this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getVoicePitch());
        }
        super.aiStep();
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new LavaBoundPathNavigation(this, level);
    }

    protected boolean canRandomSwim() {
        return true;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromBucket();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.fromBucket() && !this.hasCustomName();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource source) {
        return SoundEvents.COD_HURT;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.COD_DEATH;
    }

    protected @Nullable SoundEvent getFlopSound() {
        return SoundEvents.COD_FLOP;
    }

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        return lavaBucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return bucketItem.getDefaultInstance();
    }

    @Override
    public @NotNull SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_LAVA;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FROM_BUCKET, false);
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("FromBucket", this.fromBucket());
        compound.putDouble(tide$LENGTH_KEY, length);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setFromBucket(tag.contains("FromBucket") && tag.getBoolean("FromBucket"));
        this.length = tag.contains(tide$LENGTH_KEY) ? tag.getDouble(tide$LENGTH_KEY) : this.length;
    }

    @Override @SuppressWarnings("deprecation")
    public void saveToBucketTag(@NotNull ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack,
                tag -> tag.putDouble(tide$LENGTH_KEY, this.tide$getLength()));
    }

    @Override @SuppressWarnings("deprecation")
    public void loadFromBucketTag(@NotNull CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag(this, tag);
        this.length = tag.contains(tide$LENGTH_KEY) ? tag.getDouble(tide$LENGTH_KEY) : this.length;
    }

    @Override
    public double tide$getLength() {
        return this.length;
    }

    @Override
    public void tide$setLength(double length) {
        this.length = length;
    }

    static class LavaFishMoveControl extends MoveControl {
        private final TideLavaFish fish;

        public LavaFishMoveControl(TideLavaFish fish) {
            super(fish);
            this.fish = fish;
        }

        public void tick() {
            if (this.fish.isEyeInFluid(FluidTags.LAVA)) {
                this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0, 0.005, 0.0));
            }

            if (this.operation == Operation.MOVE_TO && !this.fish.getNavigation().isDone()) {
                float f = (float)(this.speedModifier * this.fish.getAttributeValue(Attributes.MOVEMENT_SPEED));
                this.fish.setSpeed(Mth.lerp(0.125F, this.fish.getSpeed(), f));
                double d0 = this.wantedX - this.fish.getX();
                double d1 = this.wantedY - this.fish.getY();
                double d2 = this.wantedZ - this.fish.getZ();
                if (d1 != (double)0.0F) {
                    double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0, (double)this.fish.getSpeed() * (d1 / d3) * 0.1, 0.0));
                }

                if (d0 != 0.0 || d2 != 0.0) {
                    float f1 = (float)(Mth.atan2(d2, d0) * (double)180.0F / (double)(float)Math.PI) - 90.0F;
                    this.fish.setYRot(this.rotlerp(this.fish.getYRot(), f1, 90.0F));
                    this.fish.yBodyRot = this.fish.getYRot();
                }
            }
            else this.fish.setSpeed(0.0F);
        }
    }

    static class LavaFishSwimGoal extends RandomStrollGoal {
        public LavaFishSwimGoal(TideLavaFish fish) {
            this(fish, 1.0, 40);
        }

        public LavaFishSwimGoal(TideLavaFish fish, double speedModifier, int interval) {
            super(fish, speedModifier, interval);
        }

        @Override
        public boolean canUse() {
            if (!this.mob.isInLava() || this.mob.isPassenger() || mob.getTarget() != null) return false;
            if (!this.forceTrigger && this.mob.getRandom().nextInt(this.interval) >= 100) return false;
            if (!((TideLavaFish)mob).canRandomSwim()) return false;

            Vec3 pos = this.getPosition();
            if (pos == null) return false;

            this.wantedX = pos.x;
            this.wantedY = pos.y;
            this.wantedZ = pos.z;

            this.forceTrigger = false;
            return true;
        }

        @Override
        protected Vec3 getPosition() {
            Vec3 pos = DefaultRandomPos.getPos(this.mob, 10, 7);
            for (int i = 0; pos != null && !this.mob.level()
                    .getFluidState(BlockPos.containing(pos)).is(FluidTags.LAVA) && i < 9; i++)
                pos = DefaultRandomPos.getPos(this.mob, 10, 7);
            return pos;
        }
    }

    public static <T extends LivingEntity & Bucketable> Optional<InteractionResult> lavaBucketMobPickup(Player player, InteractionHand hand, T entity) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(TideTags.Items.LAVA_BUCKETS) && entity.isAlive()) {
            ItemStack filledBucket = entity.getBucketItemStack();
            entity.saveToBucketTag(filledBucket);
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, filledBucket, false));

            if (!entity.level().isClientSide) CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, filledBucket);

            entity.playSound(entity.getPickupSound(), 1.0F, 1.0F);
            entity.discard();

            return Optional.of(InteractionResult.SUCCESS);
        }
        else return Optional.empty();
    }
}