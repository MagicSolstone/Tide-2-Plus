package com.li64.tide.util;

import com.li64.tide.Tide;
import com.li64.tide.data.TideLootTables;
import com.li64.tide.data.TideTags;
import com.li64.tide.data.fishing.FishData;
import com.li64.tide.data.item.TideItemData;
import com.li64.tide.data.player.TidePlayerData;
import com.li64.tide.network.messages.ShowToastMsg;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;


import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

public class TideUtils {
    public static boolean isJournalFish(ItemStack fish) {
        return isJournalFish(fish.getItem());
    }

    public static boolean isJournalFish(Item fish) {
        return FishData.get(fish).map(FishData::showInJournal).orElse(false);
    }

    public static Component removeRawTextInName(Component initialName) {
        String toReplace = Component.translatable("journal.remove_from_names").getString();
        String filtered = Component.translatable(initialName.getString()).getString().replace(toReplace, "");
        return Component.literal(filtered.substring(0, 1).toUpperCase() + filtered.substring(1));
    }

    public static void showFishToast(ItemStack stack, ServerPlayer player) {
        Tide.NETWORK.sendToPlayer(new ShowToastMsg(
                Component.translatable("newfish.toast.title"),
                TideUtils.removeRawTextInName(stack.getHoverName()),
                stack), player);
    }

    public static Item itemFromLocation(Identifier location) {
        return BuiltInRegistries.ITEM.getOptional(location).orElse(
                BuiltInRegistries.ITEM.getValue(BuiltInRegistries.ITEM.getKey(Items.AIR)));
    }

    public static Optional<FishData> getStrongest(List<ItemStack> hookedItems) {
        float bestStrength = 0.0f;
        FishData best = null;

        for (ItemStack hookedItem : hookedItems) {
            Optional<FishData> data = FishData.get(hookedItem);
            if (data.isEmpty()) continue;

            float strength = data.get().strength();
            if (strength >= bestStrength) {
                bestStrength = strength;
                best = data.orElse(null);
            }
        }

        return Optional.ofNullable(best);
    }

    public static ItemStack getBonusGoldItem(MinecraftServer server, LootParams params) {
        LootTable table = TideLootTables.Fishing.BONUS_GOLD.getTable(server);
        if (table == LootTable.EMPTY) return null;
        return table.getRandomItems(params).get(0);
    }

    public static ItemStack getBonusVillageItem(MinecraftServer server, LootParams params) {
        LootTable table = TideLootTables.Fishing.BONUS_VILLAGE.getTable(server);
        if (table == LootTable.EMPTY) return null;
        return table.getRandomItems(params).get(0);
    }

    @SuppressWarnings("UnusedReturnValue")
    public static boolean tryLogCatch(ItemStack stack, ServerPlayer player) {
        if (!TideUtils.isJournalFish(stack)) return false;
        TidePlayerData data = TidePlayerData.getOrCreate(player);
        data.logCatch(stack, player, player.level());
        data.syncTo(player);
        return true;
    }

    public static Optional<Holder<Biome>> findClosestNonWaterBiome(Level level, BlockPos origin, int radius, int step) {
        int steps = Math.floorDiv(radius, step);
        int[] relativeYs = Mth.outFromOrigin(0, -radius, radius, step).toArray();

        for (BlockPos.MutableBlockPos mPos : BlockPos.spiralAround(BlockPos.ZERO, steps, Direction.EAST, Direction.SOUTH)) {
            int x = origin.getX() + mPos.getX() * step;
            int z = origin.getZ() + mPos.getZ() * step;
            int qx = QuartPos.fromBlock(x);
            int qz = QuartPos.fromBlock(z);

            for (int y : relativeYs) {
                int qy = QuartPos.fromBlock(y + origin.getY());
                Holder<Biome> biome = level.getNoiseBiome(qx, qy, qz);
                if (!biome.is(TideTags.Biomes.WATER_BIOMES)) return Optional.of(biome);
            }
        }

        return Optional.empty();
    }

    public static float getTemperatureAt(BlockPos pos, ServerLevel level) {
        Climate.Sampler sampler = level.getChunkSource().randomState().createClimateSampler(net.minecraft.world.level.levelgen.densityfunction.SamplerContext.EMPTY_UNCACHED);
        int x = QuartPos.fromBlock(pos.getX());
        int y = QuartPos.fromBlock(pos.getY());
        int z = QuartPos.fromBlock(pos.getZ());
        Climate.TargetPoint targetPoint = sampler.sample(x, y, z);
        return Climate.unquantizeCoord(targetPoint.temperature());
    }

    public static MutableComponent getFormattedLength(ItemStack stack) {
        return getFormattedLength(TideItemData.FISH_LENGTH.getOrDefault(stack, 0.0));
    }

    public static MutableComponent getFormattedLength(double length) {
        if (Tide.CLIENT_CONFIG != null && Tide.CLIENT_CONFIG.journal.useImperial) return Component.literal(imperialLength(length));
        String formatted;
        if (length >= 100.0) {
            // m, two decimal places, no trailing zeros
            DecimalFormat metersFormat = new DecimalFormat("#.##");
            formatted = metersFormat.format(length / 100.0) + " m";
        } else {
            // cm, one decimal place, no trailing zeros
            DecimalFormat cmFormat = new DecimalFormat("#.#");
            formatted = cmFormat.format(length) + " cm";
        }
        return Component.literal(formatted);
    }

    /** A length in cm as inches ("8.5 in"), or feet and inches from a foot up ("2 ft 3 in"). */
    public static String imperialLength(double cm) {
        double inches = cm / 2.54;
        if (inches < 12.0) return new DecimalFormat("#.#").format(inches) + " in";
        long total = Math.round(inches);
        long feet = total / 12, rest = total % 12;
        return rest == 0 ? feet + " ft" : feet + " ft " + rest + " in";
    }

    /** A depth in metres (blocks) in the player's units. */
    public static String formatDepth(int meters) {
        if (Tide.CLIENT_CONFIG != null && Tide.CLIENT_CONFIG.journal.useImperial) return Math.round(meters * 3.28084) + " ft";
        return meters + " m";
    }

    public static float mcTempToRealTemp(float mcTemp) {
        float celsius = (float)(11 * Math.pow(mcTemp - 0.23, 3) + 30 * mcTemp + 15);
        if (Tide.CLIENT_CONFIG.journal.useFahrenheit) return celsius * 1.8f + 32f;
        return celsius;
    }

    public static String ticksToRealTime(long time, boolean useAmPm) {
        double timeInHours = (time / 1000.0) + 6;
        if (timeInHours >= 24) timeInHours -= 24;

        int hour = (int) Math.floor(timeInHours);
        int minute = (int) Math.floor((timeInHours - hour) * 60);

        if (useAmPm) {
            String amPm = (hour < 12) ? "AM" : "PM";
            int hour12 = hour % 12;
            if (hour12 == 0) hour12 = 12;
            return String.format("%d:%02d %s", hour12, minute, amPm);
        }
        return String.format("%02d:%02d", hour, minute);
    }

    public static Identifier sprite(String path) {
         return Tide.resource(path);
    }

    public static Identifier holderId(Holder<Item> holder) {
        return holder.unwrap().map(ResourceKey::identifier, BuiltInRegistries.ITEM::getKey);
    }

    public static long getTimeOfDay(ServerLevel level) {
        return level.getOverworldClockTime() % 24000;
    }

    public static LootTable getLootTable(ResourceKey<LootTable> key, MinecraftServer server) {
        return server.reloadableRegistries().getLootTable(key);
    }
}
