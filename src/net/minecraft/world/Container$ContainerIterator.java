package net.minecraft.world;

import java.util.Iterator;
import java.util.NoSuchElementException;
import net.minecraft.world.item.ItemStack;

public class Container$ContainerIterator implements Iterator<ItemStack> {
   private final Container container;
   private int index;
   private final int size;

   public Container$ContainerIterator(final Container container) {
      this.container = container;
      this.size = container.getContainerSize();
   }

   @Override
   public boolean hasNext() {
      return this.index < this.size;
   }

   public ItemStack next() {
      if (!this.hasNext()) {
         throw new NoSuchElementException();
      } else {
         return this.container.getItem(this.index++);
      }
   }
}
