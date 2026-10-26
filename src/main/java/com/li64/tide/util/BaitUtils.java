package com.li64.tide.util;

import com.google.common.collect.ImmutableList;
import com.li64.tide.data.TideData;
import com.li64.tide.data.item.TideItemData;
import com.li64.tide.data.rods.BaitContents;
import com.li64.tide.data.rods.BaitData;
import com.li64.tide.registries.TideItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BaitUtils {
    public static boolean hasBait(ItemStack rod) {
        if (rod == null) return false;
        return !getBaitItems(rod).isEmpty();
    }

    public static boolean hasBait(Item bait, ItemStack rod) {
        if (rod == null) return false;
        return getBaitItems(rod).stream().anyMatch(s -> s.is(bait));
    }

    public static List<ItemStack> getBaitItems(ItemStack rod) {
        if (rod == null) return List.of();
        BaitContents contents = TideItemData.BAIT_CONTENTS.getOrDefault(rod, new BaitContents());
        return contents.items().stream().filter(s -> !s.isEmpty()).toList();
    }

    public static boolean isBait(ItemStack stack) {
        return TideData.BAIT.get().stream().anyMatch(baitData -> stack.is(baitData.getItem()));
    }

    public static Optional<BaitData> getBaitData(ItemStack stack) {
        return TideData.BAIT.get().stream()
                .filter(baitData -> stack.is(baitData.getItem()))
                .findFirst();
    }

    public static int getBaitSpeed(ItemStack stack) {
        return getBaitData(stack).map(BaitData::speedBonus).orElse(0);
    }

    public static int getBaitLuck(ItemStack stack) {
        return getBaitData(stack).map(BaitData::luckBonus).orElse(0);
    }

    public static int getCrateChance(ItemStack stack) {
        return stack.is(TideItems.MAGNETIC_BAIT) ? 25 : 0;
    }

    public static int getCombinedSpeed(ItemStack rod) {
        return getBaitItems(rod).stream().mapToInt(BaitUtils::getBaitSpeed).sum();
    }

    public static int getCombinedLuck(ItemStack rod) {
        return getBaitItems(rod).stream().mapToInt(BaitUtils::getBaitLuck).sum();
    }

    public static List<Component> getDescriptionLines(ItemStack bait) {
        ArrayList<Component> builder = new ArrayList<>();
        if (!isBait(bait)) return ImmutableList.copyOf(builder);

        int speed = getBaitSpeed(bait);
        int luck = getBaitLuck(bait);
        int crateChance = getCrateChance(bait);

        // speed and luck points shown the way the rod panel shows them: bite speed and rare finds, as percentages
        if (speed != 0)
            builder.add(Component.translatable("text.tide.bait_tooltip.speed",
                    String.format("%+d%%", Math.round(speed * com.li64.tide.data.rods.RodEffects.SPEED_POINT * 100))).withStyle(ChatFormatting.BLUE));
        if (luck != 0)
            builder.add(Component.translatable("text.tide.bait_tooltip.lucky",
                    String.format("%+d%%", Math.round(luck * com.li64.tide.data.rods.RodEffects.RARE_PER_LUCK * 100))).withStyle(ChatFormatting.BLUE));
        if (crateChance != 0)
            builder.add(Component.translatable("text.tide.bait_tooltip.crate",
                    (crateChance < 0 ? "" : "+") + crateChance + "%").withStyle(ChatFormatting.BLUE));

        if (!builder.isEmpty()) builder.add(0, Component.translatable("text.tide.bait_tooltip.prefix").withStyle(ChatFormatting.GRAY));
        else builder.add(0, Component.translatable("text.tide.bait_tooltip.unknown_effects").withStyle(ChatFormatting.GRAY));

        // new line should be placed before text
        builder.add(0, Component.empty());
        return ImmutableList.copyOf(builder);
    }

    public static Optional<ItemStack> getFirstBaitItem(ItemStack rod) {
        List<ItemStack> baitItems = getBaitItems(rod);
        if (baitItems.isEmpty()) return Optional.empty();
        return Optional.ofNullable(baitItems.get(0));
    }
}
