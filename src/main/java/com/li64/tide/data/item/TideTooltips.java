package com.li64.tide.data.item;

import com.li64.tide.Tide;
import com.li64.tide.config.TideServerConfig;
import com.li64.tide.data.TideTags;
import com.li64.tide.data.item.TideItemData;
import com.li64.tide.registries.items.TideFishingRodItem;
import com.li64.tide.util.BaitUtils;
import com.li64.tide.util.TideUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

import java.util.List;

/**
 * Tide's own tooltip lines for an item (rod stats, perks, rigs, bait, fish quality and size). The client adds them
 * to every tooltip; {@code /fishing describe} sends them to whoever asks (Solstone shows them in its item picker).
 */
public class TideTooltips {
    public static void add(ItemStack stack, List<Component> lines, boolean inWorld) {
        if (BaitUtils.isBait(stack)) lines.addAll(BaitUtils.getDescriptionLines(stack));

        if (stack.is(TideTags.Items.FISHING_RODS)) {
            if (TideFishingRodItem.isBroken(stack))
                lines.add(Component.literal("Broken: repair it with string at an anvil (its gear stays on)").withStyle(ChatFormatting.RED));
            lines.addAll(com.li64.tide.data.rods.RodEffects.describeRod(stack));
            lines.addAll(TideFishingRodItem.getDescriptionLines(stack));
        }
        else lines.addAll(com.li64.tide.data.rods.RodEffects.describeItem(stack));   // a line, hook, bobber or gear
        if (com.li64.tide.data.rods.RodEffects.isGear(stack)) {
            lines.add(Component.literal("Full fishing set:").withStyle(ChatFormatting.GRAY));
            lines.addAll(com.li64.tide.data.rods.RodEffects.FULL_SET.describe(ChatFormatting.GOLD));
        }

        if (inWorld
                && TideItemData.IS_BUCKETABLE.getOrDefault(stack, false)
                && Tide.SERVER_CONFIG.items.bucketableFishItems == TideServerConfig.Items.BucketableMode.WHEN_LIVING)
            lines.add(Component.translatable("text.tide.fish.alive").withStyle(ChatFormatting.GRAY));

        if (TideItemData.FISH_QUALITY.isPresent(stack) && stack.has(DataComponents.CONSUMABLE) && !TideItemData.FISH_LENGTH.isPresent(stack)
                && !com.li64.tide.data.fishing.FishData.get(stack).isPresent()) {           // a cooked graded fish
            lines.add(com.li64.tide.data.angler.CatchQuality.byId(TideItemData.FISH_QUALITY.getOrDefault(stack, 1)).describe());
            lines.addAll(com.li64.tide.data.angler.CookedFish.describe(stack));
        }
        else if (TideItemData.FISH_LENGTH.isPresent(stack) || TideItemData.FISH_QUALITY.isPresent(stack)) {
            addFishLines(lines, TideItemData.FISH_QUALITY.getOrDefault(stack, -1),
                    TideItemData.FISH_LENGTH.getOrDefault(stack, 0.0), TideItemData.FISH_WEIGHT.getOrDefault(stack, 0.0));
        }
        else if (stack.has(DataComponents.BUCKET_ENTITY_DATA)) {            // a fish in a bucket
            net.minecraft.nbt.CompoundTag tag = stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY).copyTag();
            addFishLines(lines, tag.getIntOr(com.li64.tide.data.FishLengthHolder.tide$QUALITY_KEY, -1),
                    tag.getDoubleOr(com.li64.tide.data.FishLengthHolder.tide$LENGTH_KEY, 0.0),
                    tag.getDoubleOr(com.li64.tide.data.FishLengthHolder.tide$WEIGHT_KEY, 0.0));
        }
    }

    /** "◆◆◇◇◇ Good" and "3.4 lb · 19 in long" (the weight first, in gold: it's what records go by). */
    private static void addFishLines(List<Component> lines, int quality, double lengthCm, double grams) {
        if (quality >= 0) lines.add(com.li64.tide.data.angler.CatchQuality.byId(quality).describe());
        if (lengthCm <= 0 && grams <= 0) return;
        net.minecraft.network.chat.MutableComponent size = Component.empty();
        if (grams > 0) size.append(Component.literal(com.li64.tide.data.angler.FishWeight.format(grams)).withStyle(ChatFormatting.GOLD));
        // the length only for fish caught before weights (weight is what records and the wall go by)
        else if (lengthCm > 0) size.append(TideUtils.getFormattedLength(lengthCm).copy().withStyle(ChatFormatting.BLUE));
        lines.add(size);
    }
}
