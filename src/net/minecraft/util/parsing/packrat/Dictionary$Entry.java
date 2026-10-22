package net.minecraft.util.parsing.packrat;

import java.util.Objects;
import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;

class Dictionary$Entry<S, T> implements NamedRule<S, T>, Supplier<String> {
   private final Atom<T> name;
   @Nullable
   private Rule<S, T> value;

   private Dictionary$Entry(final Atom<T> name) {
      this.name = name;
   }

   @Override
   public Atom<T> name() {
      return this.name;
   }

   @Override
   public Rule<S, T> value() {
      return Objects.requireNonNull(this.value, this);
   }

   public String get() {
      return "Unbound rule " + this.name;
   }
}
