package com.li64.tide.mixin;

import com.li64.tide.data.FishLengthHolder;
import com.li64.tide.data.fishing.FishData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.fish.AbstractFish;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

@Mixin(AbstractFish.class)
public abstract class AbstractFishMixin implements FishLengthHolder {
    @Unique public double tide$length = 0.0;

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData(net.minecraft.world.level.storage.ValueInput input, CallbackInfo ci) {
        FishData data = FishData.get((Entity)(Object)this).orElse(null);
        if (data == null) return;

        this.tide$length = input.getDoubleOr(tide$LENGTH_KEY, this.tide$getLength());
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(net.minecraft.world.level.storage.ValueOutput output, CallbackInfo ci) {
        if (FishData.get((Entity)(Object)this).isEmpty()) return;
        output.putDouble(tide$LENGTH_KEY, this.tide$getLength());
    }

    @Inject(method = "saveToBucketTag", at = @At("TAIL"))
    private void saveToBucketTag(ItemStack stack, CallbackInfo ci) {
        if (FishData.get((Entity)(Object)this).isEmpty()) return;
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, tag ->
                tag.putDouble(tide$LENGTH_KEY, this.tide$getLength()));
    }

    @Inject(method = "loadFromBucketTag", at = @At("TAIL"))
    private void loadFromBucketTag(CompoundTag tag, CallbackInfo ci) {
        FishData data = FishData.get((Entity)(Object)this).orElse(null);
        if (data == null) return;

        this.tide$length = tag.getDoubleOr(tide$LENGTH_KEY, this.tide$getLength());
    }

    @Unique
    private void tide$randomizeLength() {
        Entity self = (Entity)(Object)this;
        this.tide$length = FishData.get(self)
                .map(data -> data.getRandomLength(self.getRandom()))
                .orElse(0.0);
    }

    @Override
    public double tide$getLength() {
        if (this.tide$length == 0.0) this.tide$randomizeLength();
        return this.tide$length;
    }

    @Override
    public void tide$setLength(double length) {
        this.tide$length = length;
    }
}
