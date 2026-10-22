package com.li64.tide.compat;

import com.li64.tide.registries.entities.misc.fishing.HookAccessor;
import com.li64.tide.registries.entities.misc.fishing.TideFishingHook;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Hooks for other fishing mods. This Fabric 26.x fork carries no third-party integrations,
 * so every hook reports "not loaded" and does nothing; the call sites stay as in upstream.
 */
public class CompatHelper {
    public static boolean isHybridAquaticLoaded() { return false; }

    public static Entity hybridAquaticPullEntity(ItemEntity itemEntity, Player player, TideFishingHook hook) { return itemEntity; }

    public static void hybridAquaticApplyVariant(Entity entity, ItemStack stack) {}

    public static boolean useStarcatcherMinigame() { return false; }

    public static boolean starcatcherStartMinigame(ServerPlayer player, HookAccessor hook, ItemStack rod, List<ItemStack> hookedItems) { return false; }

    public static void starcatcherCompleteCatch(ServerPlayer player, TideFishingHook hook, boolean perfectCatch) {}

    public static boolean useStardewMinigame() { return false; }

    public static boolean stardewFishingStartMinigame(ServerPlayer player, HookAccessor hook, ItemStack rod, List<ItemStack> hookedItems) { return false; }

    public static List<ItemStack> stardewFishingGetRewards(HookAccessor hook) { return List.of(); }

    public static double stardewFishingBiteTimeMultiplier() { return 1.0; }

    public static Entity fishingRealConvertItemStack(ItemStack stack, Player player, Vec3 pos) { return null; }

    public static void addInformationItemsFromAccessories(Player player, ArrayList<Item> items) {}
}
