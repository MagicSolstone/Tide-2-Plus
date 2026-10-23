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

        if (stack.is(TideTags.Items.FISHING_RODS)) lines.addAll(TideFishingRodItem.getDescriptionLines(stack));
        else lines.addAll(com.li64.tide.data.rods.RodEffects.of(stack).describe(ChatFormatting.DARK_AQUA));   // a line, hook or bobber

        if (Minecraft.getInstance().level != null
                && TideItemData.IS_BUCKETABLE.getOrDefault(stack, false)
                && Tide.SERVER_CONFIG.items.bucketableFishItems == TideServerConfig.Items.BucketableMode.WHEN_LIVING)
            lines.add(Component.translatable("text.tide.fish.alive").withStyle(ChatFormatting.GRAY));

        if (TideItemData.FISH_LENGTH.isPresent(stack) || TideItemData.FISH_QUALITY.isPresent(stack)) {
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

    /** "◆◆◇◇◇ Fine" and "19 in · 3.4 lb" (either line only when known). */
    private static void addFishLines(List<Component> lines, int quality, double lengthCm, double grams) {
        if (quality >= 0) lines.add(com.li64.tide.data.angler.CatchQuality.byId(quality).describe());
        if (lengthCm <= 0 && grams <= 0) return;
        net.minecraft.network.chat.MutableComponent size = lengthCm > 0 ? TideUtils.getFormattedLength(lengthCm) : Component.empty();
        if (grams > 0) size.append((lengthCm > 0 ? " · " : "") + com.li64.tide.data.angler.FishWeight.format(grams));
        lines.add(size.withStyle(ChatFormatting.BLUE));
    }
}
