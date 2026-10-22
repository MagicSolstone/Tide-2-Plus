package com.li64.tide.registries.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;

public class BlazingSwordfishItem extends SwordfishItem {
    public BlazingSwordfishItem(ToolMaterial tier, int damageMod, float speedMod, Properties properties) {
        super(tier, damageMod, speedMod, properties);
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.igniteForTicks(80);
        super.hurtEnemy(stack, target, attacker);
    }
}
