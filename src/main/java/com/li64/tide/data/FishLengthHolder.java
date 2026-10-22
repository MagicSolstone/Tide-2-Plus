package com.li64.tide.data;

import com.li64.tide.data.angler.CatchQuality;
import com.li64.tide.data.item.TideItemData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * A fish (entity) that remembers its size. Besides length it carries the catch quality and
 * weight, so a graded fish keeps them through buckets: item -> bucket -> fish -> bucket -> item.
 */
public interface FishLengthHolder {
    String tide$LENGTH_KEY = "FishLength";
    String tide$QUALITY_KEY = "FishQuality";
    String tide$WEIGHT_KEY = "FishWeight";

    double tide$getLength();

    void tide$setLength(double length);

    /** -1 when the fish has no quality (not caught on a rod, or caught before quality existed). */
    int tide$getQuality();

    void tide$setQuality(int quality);

    /** Grams; 0 when not weighed. */
    double tide$getWeight();

    void tide$setWeight(double grams);

    // ---------------------------------------------------------------- copying quality and weight around

    default void tide$writeExtras(CompoundTag tag) {
        if (tide$getQuality() >= 0) tag.putInt(tide$QUALITY_KEY, tide$getQuality());
        if (tide$getWeight() > 0) tag.putDouble(tide$WEIGHT_KEY, tide$getWeight());
    }

    default void tide$readExtras(CompoundTag tag) {
        tide$setQuality(tag.getIntOr(tide$QUALITY_KEY, tide$getQuality()));
        tide$setWeight(tag.getDoubleOr(tide$WEIGHT_KEY, tide$getWeight()));
    }

    default void tide$writeExtras(ValueOutput out) {
        if (tide$getQuality() >= 0) out.putInt(tide$QUALITY_KEY, tide$getQuality());
        if (tide$getWeight() > 0) out.putDouble(tide$WEIGHT_KEY, tide$getWeight());
    }

    default void tide$readExtras(ValueInput in) {
        tide$setQuality(in.getIntOr(tide$QUALITY_KEY, tide$getQuality()));
        tide$setWeight(in.getDoubleOr(tide$WEIGHT_KEY, tide$getWeight()));
    }

    /** Onto a fish item made from this fish (bucket emptied into an item, or the fish dropped). */
    default void tide$applyExtras(ItemStack stack) {
        if (tide$getQuality() >= 0) CatchQuality.mark(stack, CatchQuality.byId(tide$getQuality()));
        if (tide$getWeight() > 0) TideItemData.FISH_WEIGHT.set(stack, tide$getWeight());
    }

    /** From a fish item's quality and weight into a bucket's saved fish. */
    static void copyItemToTag(ItemStack fish, CompoundTag tag) {
        Integer q = TideItemData.FISH_QUALITY.getOrDefault(fish, null);
        if (q != null) tag.putInt(tide$QUALITY_KEY, q);
        double grams = TideItemData.FISH_WEIGHT.getOrDefault(fish, 0.0);
        if (grams > 0) tag.putDouble(tide$WEIGHT_KEY, grams);
    }
}
