package com.li64.tide.registries.items;

import net.minecraft.world.item.Item;

/** A lure: goes in the angling table's lure slot, never used up, and changes which fish come (see Lures). */
public class FishingLureItem extends Item {
    public FishingLureItem(Properties properties) {
        super(properties.stacksTo(16));
    }
}
