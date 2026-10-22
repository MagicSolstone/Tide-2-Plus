package net.minecraft.util.context;

import com.google.common.collect.Sets;
import java.util.Set;

public class ContextKeySet$Builder {
   private final Set<ContextKey<?>> required = Sets.newIdentityHashSet();
   private final Set<ContextKey<?>> optional = Sets.newIdentityHashSet();

   public ContextKeySet$Builder required(final ContextKey<?> param) {
      if (this.optional.contains(param)) {
         throw new IllegalArgumentException("Parameter " + param.name() + " is already optional");
      } else {
         this.required.add(param);
         return this;
      }
   }

   public ContextKeySet$Builder optional(final ContextKey<?> param) {
      if (this.required.contains(param)) {
         throw new IllegalArgumentException("Parameter " + param.name() + " is already required");
      } else {
         this.optional.add(param);
         return this;
      }
   }

   public ContextKeySet build() {
      return new ContextKeySet(this.required, this.optional);
   }
}
