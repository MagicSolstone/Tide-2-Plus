package net.minecraft.world.item.component;

import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface UseRemainder$OnExtraCreatedRemainder {
   void apply(final ItemStack extraCreatedRemainder);
}
