package com.li64.tide.registries.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

/** A sword (26.x has no SwordItem: a sword is an item with sword properties). Not repairable. */
public class SwordfishItem extends Item {
    public SwordfishItem(ToolMaterial material, int damageMod, float speedMod, Properties properties) {
        super(properties.sword(material, damageMod, speedMod).repairable(net.minecraft.tags.TagKey.create(
                net.minecraft.core.registries.Registries.ITEM, com.li64.tide.Tide.resource("never_repairs"))));
    }
}
