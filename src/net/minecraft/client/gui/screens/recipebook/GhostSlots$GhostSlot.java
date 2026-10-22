package net.minecraft.client.gui.screens.recipebook;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
record GhostSlots$GhostSlot(List<ItemStack> items, boolean isResultSlot) {
   public ItemStack getItem(final int itemIndex) {
      int size = this.items.size();
      return size == 0 ? ItemStack.EMPTY : this.items.get(itemIndex % size);
   }
}
