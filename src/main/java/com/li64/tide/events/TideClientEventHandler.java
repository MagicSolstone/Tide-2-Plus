package com.li64.tide.events;

import com.li64.tide.Tide;
import com.li64.tide.config.TideServerConfig;
import com.li64.tide.data.TideTags;
import com.li64.tide.data.item.TideItemData;
import com.li64.tide.registries.items.TideFishingRodItem;
import com.li64.tide.util.BaitUtils;
import com.li64.tide.util.TideUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

import java.util.List;

public class TideClientEventHandler {
    public static void onTooltipRender(ItemStack stack, List<Component> lines) {
        if (BaitUtils.isBait(stack)) lines.addAll(BaitUtils.getDescriptionLines(stack));

        if (stack.is(TideTags.Items.FISHING_RODS)) {
            lines.addAll(com.li64.tide.data.rods.RodEffects.describeRod(stack));
            lines.addAll(TideFishingRodItem.getDescriptionLines(stack));
        }
        else lines.addAll(com.li64.tide.data.rods.RodEffects.describeItem(stack));   // a line, hook, bobber or gear
        if (com.li64.tide.data.rods.RodEffects.isGear(stack)) {
            lines.add(Component.literal("Full fishing set:").withStyle(ChatFormatting.GRAY));
            lines.addAll(com.li64.tide.data.rods.RodEffects.FULL_SET.describe(ChatFormatting.GOLD));
        }

        if (Minecraft.getInstance().level != null
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
        if (lengthCm > 0) size.append(Component.literal(grams > 0 ? " · " : "").withStyle(ChatFormatting.BLUE))
                .append(TideUtils.getFormattedLength(lengthCm).copy().append(grams > 0 ? " long" : "").withStyle(ChatFormatting.BLUE));
        lines.add(size);
    }
}
