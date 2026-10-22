package net.minecraft.util;

import java.util.Iterator;
import java.util.Objects;

class ArrayListDeque$DescendingIterator implements Iterator<T> {
   private int index;

   public ArrayListDeque$DescendingIterator(final ArrayListDeque this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.index = this$0x.size() - 1;
   }

   @Override
   public boolean hasNext() {
      return this.index >= 0;
   }

   @Override
   public T next() {
      return (T)this.this$0.get(this.index--);
   }

   @Override
   public void remove() {
      this.this$0.remove(this.index + 1);
   }
}
