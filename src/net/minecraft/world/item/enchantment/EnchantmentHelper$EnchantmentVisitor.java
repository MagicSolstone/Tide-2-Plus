package net.minecraft.world.item.enchantment;

import net.minecraft.core.Holder;

@FunctionalInterface
interface EnchantmentHelper$EnchantmentVisitor {
   void accept(Holder<Enchantment> enchantment, int level);
}
