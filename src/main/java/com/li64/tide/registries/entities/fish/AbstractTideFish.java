package com.li64.tide.registries.entities.fish;

import com.li64.tide.data.FishLengthHolder;
import com.li64.tide.data.fishing.FishData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.Bucketable;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

public abstract class AbstractTideFish extends WaterAnimal implements Bucketable, FishLengthHolder {
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(AbstractTideFish.class, EntityDataSerializers.BOOLEAN);

    private final Item bucketItem;
    private double length;
    private int quality = -1;
    private double weight = 0.0;

    public AbstractTideFish(EntityType<? extends WaterAnimal> entityType, Level level) {
        super(entityType, level);

        Identifier key = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
        Item fishItem = BuiltInRegistries.ITEM.getOptional(key).orElseThrow();
        this.bucketItem = BuiltInRegistries.ITEM.getOptional(key.withSuffix("_bucket")).orElseThrow();
        this.length = FishData.get(fishItem).map(data -> data.getRandomLength(getRandom())).orElse(0.0);
    }

    @Override
    public void travel(@NotNull Vec3 movement) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed() / 50f, movement);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
        }
        else super.travel(movement);
    }

    public boolean doesFlop() {
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
    public void aiStep() {
        if (this.doesFlop() && !this.isInLiquid() && this.onGround() && this.verticalCollision) {
            this.setDeltaMovement(this.getDeltaMovement().add(
                    (this.random.nextFloat() * 2.0 - 1.0) * 0.05, 0.4,
                    (this.random.nextFloat() * 2.0 - 1.0) * 0.05)
            );
            this.setOnGround(false);
            this.needsSync = true;
            this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getVoicePitch());
        }
        super.aiStep();
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new WaterBoundPathNavigation(this, level);
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
        return Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return bucketItem.getDefaultInstance();
    }

    @Override
    public @NotNull SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
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
    protected void addAdditionalSaveData(@NotNull net.minecraft.world.level.storage.ValueOutput compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("FromBucket", this.fromBucket());
        compound.putDouble(tide$LENGTH_KEY, this.length);
        this.tide$writeExtras(compound);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull net.minecraft.world.level.storage.ValueInput tag) {
        super.readAdditionalSaveData(tag);
        this.setFromBucket(tag.getBooleanOr("FromBucket", false));
        this.length = tag.getDoubleOr(tide$LENGTH_KEY, this.length);
        this.tide$readExtras(tag);
    }

    @Override @SuppressWarnings("deprecation")
    public void saveToBucketTag(@NotNull ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, tag -> {
            tag.putDouble(tide$LENGTH_KEY, this.tide$getLength());
            this.tide$writeExtras(tag);
        });
    }

    @Override @SuppressWarnings("deprecation")
    public void loadFromBucketTag(@NotNull CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag(this, tag);
        this.length = tag.getDoubleOr(tide$LENGTH_KEY, this.tide$getLength());
        this.tide$readExtras(tag);
    }

    @Override
    public double tide$getLength() {
        return this.length;
    }

    @Override
    public void tide$setLength(double length) {
        this.length = length;
    }

    @Override
    public int tide$getQuality() {
        return this.quality;
    }

    @Override
    public void tide$setQuality(int quality) {
        this.quality = quality;
    }

    @Override
    public double tide$getWeight() {
        return this.weight;
    }

    @Override
    public void tide$setWeight(double grams) {
        this.weight = grams;
    }
}