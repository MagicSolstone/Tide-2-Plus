package net.minecraft.advancements.predicates;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.predicates.DataComponentPredicate;
import net.minecraft.core.component.predicates.DataComponentPredicate$AnyValueType;
import net.minecraft.core.component.predicates.DataComponentPredicate$Type;

public class DataComponentMatchers$Builder {
   private DataComponentExactPredicate exact = DataComponentExactPredicate.EMPTY;
   private final Builder<DataComponentPredicate$Type<?>, DataComponentPredicate> partial = ImmutableMap.builder();

   private DataComponentMatchers$Builder() {
   }

   public static DataComponentMatchers$Builder components() {
      return new DataComponentMatchers$Builder();
   }

   public <T extends DataComponentType<?>> DataComponentMatchers$Builder any(final DataComponentType<?> type) {
      DataComponentPredicate$AnyValueType predicateType = DataComponentPredicate$AnyValueType.create(type);
      this.partial.put(predicateType, predicateType.predicate());
      return this;
   }

   public <T extends DataComponentPredicate> DataComponentMatchers$Builder partial(final DataComponentPredicate$Type<T> type, final T predicate) {
      this.partial.put(type, predicate);
      return this;
   }

   public DataComponentMatchers$Builder exact(final DataComponentExactPredicate exact) {
      this.exact = exact;
      return this;
   }

   public DataComponentMatchers build() {
      return new DataComponentMatchers(this.exact, this.partial.buildOrThrow());
   }
}
