package net.minecraft.nbt;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

class CollectionTag$1 implements Iterator<Tag> {
   private int index;

   CollectionTag$1(final CollectionTag this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public boolean hasNext() {
      return this.index < this.this$0.size();
   }

   public Tag next() {
      if (!this.hasNext()) {
         throw new NoSuchElementException();
      } else {
         return this.this$0.get(this.index++);
      }
   }
}
