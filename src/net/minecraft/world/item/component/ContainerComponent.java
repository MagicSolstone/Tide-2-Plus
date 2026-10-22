package net.minecraft.world.item.component;

import java.util.stream.Stream;
import net.minecraft.world.item.ItemStack;

public interface ContainerComponent<T extends ContainerComponent<T>> {
   Stream<ItemStack> itemCopies();

   int size();

   T copyWithContents(Stream<ItemStack> newContents);

   ContainerComponent$Mutable<T> asMutable();
}
