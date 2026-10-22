package net.minecraft.world.item.enchantment;

@FunctionalInterface
interface Enchantment$GenericAction<T> {
   void apply(T effect);
}
