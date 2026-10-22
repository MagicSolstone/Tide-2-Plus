package net.minecraft.util.context;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import java.util.Set;

public class ContextKeySet {
   private final Set<ContextKey<?>> required;
   private final Set<ContextKey<?>> allowed;

   private ContextKeySet(final Set<ContextKey<?>> required, final Set<ContextKey<?>> optional) {
      this.required = Set.copyOf(required);
      this.allowed = Set.copyOf(Sets.union(required, optional));
   }

   public Set<ContextKey<?>> required() {
      return this.required;
   }

   public Set<ContextKey<?>> allowed() {
      return this.allowed;
   }

   @Override
   public String toString() {
      return "[" + Joiner.on(", ").join(this.allowed.stream().map(k -> (this.required.contains(k) ? "!" : "") + k.name()).iterator()) + "]";
   }
}
