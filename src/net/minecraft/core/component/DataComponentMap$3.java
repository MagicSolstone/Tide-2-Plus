package net.minecraft.core.component;

import com.google.common.collect.Sets;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import org.jspecify.annotations.Nullable;

class DataComponentMap$3 implements DataComponentMap {
   DataComponentMap$3(final DataComponentMap this$0, final Predicate val$predicate) {
      this.val$predicate = val$predicatex;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Nullable
   @Override
   public <T> T get(final DataComponentType<? extends T> type) {
      return this.val$predicate.test(type) ? this.this$0.get(type) : null;
   }

   @Override
   public Set<DataComponentType<?>> keySet() {
      return Sets.filter(this.this$0.keySet(), this.val$predicate::test);
   }
}
