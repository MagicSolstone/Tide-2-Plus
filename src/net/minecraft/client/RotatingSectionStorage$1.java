package net.minecraft.client;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class RotatingSectionStorage$1 implements Iterator<T> {
   private int i;

   RotatingSectionStorage$1(final RotatingSectionStorage this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public boolean hasNext() {
      return this.i < this.this$0.nodes.length - 1;
   }

   public T next() {
      if (this.i >= this.this$0.nodes.length) {
         throw new NoSuchElementException();
      } else {
         return this.this$0.nodes[this.i++].value;
      }
   }
}
