package net.minecraft.world.level.storage;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

class ValueInputContextHelper$2 implements ValueInput$TypedInputList<Object> {
   ValueInputContextHelper$2(final ValueInputContextHelper this$0) {
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public boolean isEmpty() {
      return true;
   }

   @Override
   public Stream<Object> stream() {
      return Stream.empty();
   }

   @Override
   public Iterator<Object> iterator() {
      return Collections.emptyIterator();
   }
}
