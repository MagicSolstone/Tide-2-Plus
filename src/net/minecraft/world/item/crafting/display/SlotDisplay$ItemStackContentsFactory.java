package net.minecraft.world.item.crafting.display;

import net.minecraft.world.item.ItemStack;

public class SlotDisplay$ItemStackContentsFactory implements DisplayContentsFactory$ForStacks<ItemStack> {
   public static final SlotDisplay$ItemStackContentsFactory INSTANCE = new SlotDisplay$ItemStackContentsFactory();

   public ItemStack forStack(final ItemStack stack) {
      return stack;
   }
}
