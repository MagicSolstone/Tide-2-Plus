package com.li64.tide.client;

import com.li64.tide.data.item.TideItemData;
import com.li64.tide.registries.items.FishSatchelItem;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/** "tide:satchel_state" for items/fish_satchel.json: 0 closed, 0.3 open and empty, 0.6 one fish, 1 more. */
public record SatchelStateProperty() implements RangeSelectItemModelProperty {
    public static final MapCodec<SatchelStateProperty> MAP_CODEC = MapCodec.unit(new SatchelStateProperty());

    @Override
    public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        if (!TideItemData.FISH_SATCHEL_OPENED.getOrDefault(stack, false)) return 0f;
        int fishCount = FishSatchelItem.getFishCount(stack);
        if (fishCount > 0) return fishCount > 1 ? 1f : 0.6f;
        return 0.3f;
    }

    @Override
    public MapCodec<SatchelStateProperty> type() {
        return MAP_CODEC;
    }
}
