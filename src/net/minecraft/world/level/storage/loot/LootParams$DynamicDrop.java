package net.minecraft.world.level.storage.loot;

import java.util.function.Consumer;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface LootParams$DynamicDrop {
   void add(Consumer<ItemStack> output);
}
