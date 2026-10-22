package com.li64.tide.data.rods;

import com.google.common.collect.ImmutableList;
import com.li64.tide.data.ItemDataKey;
import com.li64.tide.data.item.TideItemData;
import com.li64.tide.registries.TideItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public class CustomRodManager {
    private static final ItemStack DEFAULT_BOBBER = TideItems.RED_BOBBER.getDefaultInstance();
    private static final ItemStack DEFAULT_HOOK = TideItems.FISHING_HOOK.getDefaultInstance();
    private static final ItemStack DEFAULT_LINE = TideItems.FISHING_LINE.getDefaultInstance();

    public static void setBobber(ItemStack rod, ItemStack bobber) {
        setAccessory(TideItemData.FISHING_BOBBER, rod, bobber);
    }

    public static void setHook(ItemStack rod, ItemStack hook) {
        setAccessory(TideItemData.FISHING_HOOK, rod, hook);
    }

    public static void setLine(ItemStack rod, ItemStack line) {
        setAccessory(TideItemData.FISHING_LINE, rod, line);
    }

    private static void setAccessory(ItemDataKey<CompoundTag> dataKey, ItemStack rod, ItemStack accessory) {
        if (accessory == null || accessory.isEmpty()) {
            dataKey.set(rod, new CompoundTag());
            return;
        }
        dataKey.set(rod, (CompoundTag) ItemStack.CODEC.encode(accessory, NbtOps.INSTANCE, new CompoundTag()).getOrThrow());
         /*dataKey.set(rod, accessory.save(new CompoundTag()));*/
    }

    public static ItemStack getBobber(ItemStack rod) {
        return getAccessory(TideItemData.FISHING_BOBBER, rod, DEFAULT_BOBBER);
    }

    public static ItemStack getHook(ItemStack rod) {
        return getAccessory(TideItemData.FISHING_HOOK, rod, DEFAULT_HOOK);
    }

    public static ItemStack getLine(ItemStack rod) {
        return getAccessory(TideItemData.FISHING_LINE, rod, DEFAULT_LINE);
    }

    public static boolean hasBobber(ItemStack rod) {
        return getAccessory(TideItemData.FISHING_BOBBER, rod) != null;
    }

    public static boolean hasHook(ItemStack rod) {
        return getAccessory(TideItemData.FISHING_HOOK, rod) != null;
    }

    public static boolean hasLine(ItemStack rod) {
        return getAccessory(TideItemData.FISHING_LINE, rod) != null;
    }

    private static ItemStack getAccessory(ItemDataKey<CompoundTag> dataKey, ItemStack rod) {
        return getAccessory(dataKey, rod, null);
    }

    private static ItemStack getAccessory(ItemDataKey<CompoundTag> dataKey, ItemStack rod, ItemStack defaultItem) {
        CompoundTag data = dataKey.get(rod);
        if (data == null || data.isEmpty()) return defaultItem;
        Optional<ItemStack> accessory = ItemStack.CODEC.parse(NbtOps.INSTANCE, data).result();
        return accessory.orElse(defaultItem);
    }

    public static List<ItemStack> getAccessoryList(ItemStack stack) {
        ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();
        if (hasBobber(stack)) builder.add(getBobber(stack));
        if (hasHook(stack)) builder.add(getHook(stack));
        if (hasLine(stack)) builder.add(getLine(stack));
        return builder.build();
    }

    @Deprecated(forRemoval = true, since = "2.0")
    public static boolean hasBobber(ItemStack rod, HolderLookup.Provider registryAccess) {
        return getAccessory(TideItemData.FISHING_BOBBER, rod) != null;
    }

    /**
     * Kept for Stardew Fishing support
     * @deprecated The version of this method without registryAccess should be used instead
     */
    @Deprecated(forRemoval = true, since = "2.0")
    public static ItemStack getBobber(ItemStack rod, HolderLookup.Provider registryAccess) {
        return getAccessory(TideItemData.FISHING_BOBBER, rod, DEFAULT_BOBBER);
    }

    /**
     * Kept for Stardew Fishing support
     * @deprecated The version of this method without registryAccess should be used instead
     */
    @Deprecated(forRemoval = true, since = "2.0")
    public static void setBobber(ItemStack rod, ItemStack bobber, HolderLookup.Provider registryAccess) {
        setAccessory(TideItemData.FISHING_BOBBER, rod, bobber);
    }

    /**
     * Kept for Stardew Fishing support
     * @deprecated The version of this method without registryAccess should be used instead
     */
    @Deprecated(forRemoval = true, since = "2.0")
    public static boolean hasHook(ItemStack rod, HolderLookup.Provider registryAccess) {
        return getAccessory(TideItemData.FISHING_HOOK, rod) != null;
    }

    /**
     * Kept for Stardew Fishing support
     * @deprecated The version of this method without registryAccess should be used instead
     */
    @Deprecated(forRemoval = true, since = "2.0")
    public static ItemStack getHook(ItemStack rod, HolderLookup.Provider registryAccess) {
        return getAccessory(TideItemData.FISHING_HOOK, rod, DEFAULT_HOOK);
    }

    /**
     * Kept for Stardew Fishing support
     * @deprecated The version of this method without registryAccess should be used instead
     */
    @Deprecated(forRemoval = true, since = "2.0")
    public static boolean hasLine(ItemStack rod, HolderLookup.Provider registryAccess) {
        return getAccessory(TideItemData.FISHING_LINE, rod) != null;
    }

    /**
     * Kept for Stardew Fishing support
     * @deprecated The version of this method without registryAccess should be used instead
     */
    @Deprecated(forRemoval = true, since = "2.0")
    public static ItemStack getLine(ItemStack rod, HolderLookup.Provider registryAccess) {
        return getAccessory(TideItemData.FISHING_LINE, rod, DEFAULT_LINE);
    }
}
