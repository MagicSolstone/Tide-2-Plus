package com.li64.tide.registries.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;

public class BlazingSwordfishItem extends SwordfishItem {
    public BlazingSwordfishItem(Tiers tier, int damageMod, float speedMod, Properties properties) {
        super(tier, damageMod, speedMod, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.igniteForTicks(80);
        return super.hurtEnemy(stack, target, attacker);
    }
}
