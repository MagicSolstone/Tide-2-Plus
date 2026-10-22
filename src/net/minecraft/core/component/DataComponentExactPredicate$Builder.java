package net.minecraft.core.component;

import java.util.ArrayList;
import java.util.List;

public class DataComponentExactPredicate$Builder {
   private final List<TypedDataComponent<?>> expectedComponents = new ArrayList<>();

   private DataComponentExactPredicate$Builder() {
   }

   public <T> DataComponentExactPredicate$Builder expect(final TypedDataComponent<T> value) {
      return this.expect(value.type(), value.value());
   }

   public <T> DataComponentExactPredicate$Builder expect(final DataComponentType<? super T> type, final T value) {
      for (TypedDataComponent<?> component : this.expectedComponents) {
         if (component.type() == type) {
            throw new IllegalArgumentException("Predicate already has component of type: '" + type + "'");
         }
      }

      this.expectedComponents.add(new TypedDataComponent<>(type, value));
      return this;
   }

   public DataComponentExactPredicate build() {
      return new DataComponentExactPredicate(List.copyOf(this.expectedComponents));
   }
}
