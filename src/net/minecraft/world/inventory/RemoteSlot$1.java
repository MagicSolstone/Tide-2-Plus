package net.minecraft.world.inventory;

import net.minecraft.network.HashedStack;
import net.minecraft.world.item.ItemStack;

class RemoteSlot$1 implements RemoteSlot {
   @Override
   public void receive(final HashedStack incoming) {
   }

   @Override
   public void force(final ItemStack outgoing) {
   }

   @Override
   public boolean matches(final ItemStack local) {
      return true;
   }
}
