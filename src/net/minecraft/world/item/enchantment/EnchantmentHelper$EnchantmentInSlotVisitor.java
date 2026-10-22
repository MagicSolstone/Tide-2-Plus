package net.minecraft.world.item.enchantment;

import net.minecraft.core.Holder;

@FunctionalInterface
interface EnchantmentHelper$EnchantmentInSlotVisitor {
   void accept(Holder<Enchantment> enchantment, int level, EnchantedItemInUse item);
}
