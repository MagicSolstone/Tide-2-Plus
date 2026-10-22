package net.minecraft.world.level.storage;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

class ValueInputContextHelper$1 implements ValueInput$ValueInputList {
   ValueInputContextHelper$1(final ValueInputContextHelper this$0) {
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public boolean isEmpty() {
      return true;
   }

   @Override
   public Stream<ValueInput> stream() {
      return Stream.empty();
   }

   @Override
   public Iterator<ValueInput> iterator() {
      return Collections.emptyIterator();
   }
}
