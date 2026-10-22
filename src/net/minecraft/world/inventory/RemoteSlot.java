package net.minecraft.world.inventory;

import net.minecraft.network.HashedStack;
import net.minecraft.world.item.ItemStack;

public interface RemoteSlot {
   RemoteSlot PLACEHOLDER = new RemoteSlot$1();

   void force(ItemStack outgoing);

   void receive(HashedStack incoming);

   boolean matches(ItemStack local);
}
