package net.minecraft.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

class SortedArraySet$ArrayIterator implements Iterator<T> {
   private int index;
   private int last;

   private SortedArraySet$ArrayIterator(final SortedArraySet this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.last = -1;
   }

   @Override
   public boolean hasNext() {
      return this.index < this.this$0.size;
   }

   @Override
   public T next() {
      if (this.index >= this.this$0.size) {
         throw new NoSuchElementException();
      } else {
         this.last = this.index++;
         return this.this$0.contents[this.last];
      }
   }

   @Override
   public void remove() {
      if (this.last == -1) {
         throw new IllegalStateException();
      } else {
         this.this$0.removeInternal(this.last);
         this.index--;
         this.last = -1;
      }
   }
}
