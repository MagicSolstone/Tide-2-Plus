package net.minecraft.core.component;

import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.core.Holder;

record DataComponentLookup$ComponentStorage<C, T>(Multimap<C, Holder<T>> valueToComponent) {
   public Collection<Holder<T>> findAll(final C value) {
      return this.valueToComponent.get(value);
   }

   public Stream<Holder<T>> findMatching(final Predicate<C> predicate) {
      return this.valueToComponent.isEmpty()
         ? Stream.empty()
         : this.valueToComponent.entries().stream().filter(e -> predicate.test((C)e.getKey())).map(Entry::getValue);
   }
}
