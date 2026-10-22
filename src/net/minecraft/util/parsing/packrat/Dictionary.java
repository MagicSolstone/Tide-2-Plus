package net.minecraft.util.parsing.packrat;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Dictionary<S> {
   private final Map<Atom<?>, Dictionary$Entry<S, ?>> terms = new IdentityHashMap<>();

   public <T> NamedRule<S, T> put(final Atom<T> name, final Rule<S, T> entry) {
      Dictionary$Entry<S, T> holder = (Dictionary$Entry<S, T>)this.terms.computeIfAbsent(name, Dictionary$Entry::new);
      if (holder.value != null) {
         throw new IllegalArgumentException("Trying to override rule: " + name);
      } else {
         holder.value = entry;
         return holder;
      }
   }

   public <T> NamedRule<S, T> putComplex(final Atom<T> name, final Term<S> term, final Rule$RuleAction<S, T> action) {
      return this.put(name, Rule.fromTerm(term, action));
   }

   public <T> NamedRule<S, T> put(final Atom<T> name, final Term<S> term, final Rule$SimpleRuleAction<S, T> action) {
      return this.put(name, Rule.fromTerm(term, action));
   }

   public void checkAllBound() {
      List<? extends Atom<?>> unboundNames = this.terms.entrySet().stream().filter(e -> e.getValue().value == null).map(Map.Entry::getKey).toList();
      if (!unboundNames.isEmpty()) {
         throw new IllegalStateException("Unbound names: " + unboundNames);
      }
   }

   public <T> NamedRule<S, T> getOrThrow(final Atom<T> name) {
      return (NamedRule<S, T>)Objects.requireNonNull(this.terms.get(name), () -> "No rule called " + name);
   }

   public <T> NamedRule<S, T> forward(final Atom<T> name) {
      return this.getOrCreateEntry(name);
   }

   private <T> Dictionary$Entry<S, T> getOrCreateEntry(final Atom<T> name) {
      return (Dictionary$Entry<S, T>)this.terms.computeIfAbsent(name, Dictionary$Entry::new);
   }

   public <T> Term<S> named(final Atom<T> name) {
      return new Dictionary$Reference<>(this.getOrCreateEntry(name), name);
   }

   public <T> Term<S> namedWithAlias(final Atom<T> nameToParse, final Atom<T> nameToStore) {
      return new Dictionary$Reference<>(this.getOrCreateEntry(nameToParse), nameToStore);
   }
}
