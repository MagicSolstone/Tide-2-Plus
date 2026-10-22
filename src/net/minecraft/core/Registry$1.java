package net.minecraft.core;

import java.util.Iterator;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

class Registry$1 implements IdMap<Holder<T>> {
   Registry$1(final Registry this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   public int getId(final Holder<T> thing) {
      return this.this$0.getId(thing.value());
   }

   @Nullable
   public Holder<T> byId(final int id) {
      return (Holder<T>)this.this$0.get(id).orElse(null);
   }

   @Override
   public int size() {
      return this.this$0.size();
   }

   @Override
   public Iterator<Holder<T>> iterator() {
      return this.this$0.listElements().map(e -> (Holder)e).iterator();
   }
}
