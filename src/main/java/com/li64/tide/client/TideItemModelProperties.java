package com.li64.tide.client;

import com.li64.tide.Tide;
import com.li64.tide.data.item.TideItemData;
import com.li64.tide.registries.TideItems;
import com.li64.tide.registries.items.FishSatchelItem;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Items;

public class TideItemModelProperties {
    public static final Identifier CAST_PROPERTY = Tide.resource("cast");
    public static final ClampedItemPropertyFunction CAST_FUNCTION = (stack, level, player, i) -> {
        if (player == null) return 0;
        boolean flag = player.getMainHandItem() == stack;
        boolean flag1 = player.getOffhandItem() == stack;
        if (player.getMainHandItem().getItem() instanceof FishingRodItem) flag1 = false;
        return (flag || flag1) && player instanceof Player && ((Player) player).fishing != null ? 1 : 0;
    };

    public static final Identifier PULLING_PROPERTY = Tide.resource("pulling");
    public static final ClampedItemPropertyFunction PULLING_FUNCTION = (stack, level, player, i) ->
            player != null && player.isUsingItem() && player.getUseItem() == stack ? 1.0f : 0.0f;
    public static final Identifier PULL_PROPERTY = Tide.resource("pull");
    public static final ClampedItemPropertyFunction PULL_FUNCTION = (stack, level, player, i) -> {
        if (player == null) return 0f;
        return player.getUseItem() != stack ? 0f : (float)(stack.getUseDuration(player) - player.getUseItemRemainingTicks()) / 20f;
    };

    public static final Identifier SATCHEL_STATE_PROPERTY = Tide.resource("satchel_state");
    public static final ClampedItemPropertyFunction SATCHEL_STATE_FUNCTION = (stack, level, player, i) -> {
        boolean isOpen = TideItemData.FISH_SATCHEL_OPENED.getOrDefault(stack, false);
        int fishCount = FishSatchelItem.getFishCount(stack);
        if (isOpen) {
            if (fishCount > 0) return fishCount > 1 ? 1f : 0.6f;
            return 0.3f;
        }
        return 0f;
    };
    
    public static void registerAll() {
        ItemProperties.register(Items.FISHING_ROD, CAST_PROPERTY, CAST_FUNCTION);
        ItemProperties.register(TideItems.STONE_FISHING_ROD, CAST_PROPERTY, CAST_FUNCTION);
        ItemProperties.register(TideItems.IRON_FISHING_ROD, CAST_PROPERTY, CAST_FUNCTION);
        ItemProperties.register(TideItems.GOLDEN_FISHING_ROD, CAST_PROPERTY, CAST_FUNCTION);
        ItemProperties.register(TideItems.CRYSTAL_FISHING_ROD, CAST_PROPERTY, CAST_FUNCTION);
        ItemProperties.register(TideItems.DIAMOND_FISHING_ROD, CAST_PROPERTY, CAST_FUNCTION);
        ItemProperties.register(TideItems.NETHERITE_FISHING_ROD, CAST_PROPERTY, CAST_FUNCTION);
        ItemProperties.register(TideItems.ECHO_FISHING_ROD, CAST_PROPERTY, CAST_FUNCTION);
        ItemProperties.register(TideItems.PRISMARINE_FISHING_ROD, CAST_PROPERTY, CAST_FUNCTION);
        ItemProperties.register(TideItems.SUNFLOWER_FISHING_ROD, CAST_PROPERTY, CAST_FUNCTION);
        ItemProperties.register(TideItems.VILLAGE_FISHING_ROD, CAST_PROPERTY, CAST_FUNCTION);
        ItemProperties.register(TideItems.BLAZING_FISHING_ROD, CAST_PROPERTY, CAST_FUNCTION);
        ItemProperties.register(TideItems.HONEYCOMB_FISHING_ROD, CAST_PROPERTY, CAST_FUNCTION);

        ItemProperties.register(TideItems.STARLIGHT_BOW, PULLING_PROPERTY, PULLING_FUNCTION);
        ItemProperties.register(TideItems.STARLIGHT_BOW, PULL_PROPERTY, PULL_FUNCTION);

        ItemProperties.register(TideItems.FISH_SATCHEL, SATCHEL_STATE_PROPERTY, SATCHEL_STATE_FUNCTION);
    }
}
