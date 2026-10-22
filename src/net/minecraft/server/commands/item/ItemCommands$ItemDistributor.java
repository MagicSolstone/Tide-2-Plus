package net.minecraft.server.commands.item;

import java.util.Collection;
import net.minecraft.world.item.ItemProvider;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
interface ItemCommands$ItemDistributor {
   ItemProvider apply(Collection<ItemStack> items);
}
