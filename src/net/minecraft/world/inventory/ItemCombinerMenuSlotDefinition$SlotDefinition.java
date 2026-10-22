package net.minecraft.world.inventory;

import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;

public record ItemCombinerMenuSlotDefinition$SlotDefinition(int slotIndex, int x, int y, Predicate<ItemStack> mayPlace) {
   private static final ItemCombinerMenuSlotDefinition$SlotDefinition EMPTY = new ItemCombinerMenuSlotDefinition$SlotDefinition(0, 0, 0, itemStack -> true);
}
