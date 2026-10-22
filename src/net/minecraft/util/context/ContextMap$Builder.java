package net.minecraft.util.context;

import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.util.Map;
import java.util.Set;
import org.jspecify.annotations.Nullable;

public class ContextMap$Builder {
   private final Map<ContextKey<?>, Object> params = new Reference2ObjectOpenHashMap();

   private ContextMap$Builder() {
   }

   public <T> ContextMap$Builder set(final ContextKey<T> param, @Nullable final T value) {
      if (value == null) {
         this.params.remove(param);
      } else {
         this.params.put(param, value);
      }

      return this;
   }

   @Nullable
   public <T> T get(final ContextKey<T> param) {
      return (T)this.params.get(param);
   }

   public ContextMap build() {
      return this.params.isEmpty() ? ContextMap.EMPTY : new ContextMap(new Reference2ObjectOpenHashMap(this.params));
   }

   public ContextMap buildAndValidate(final ContextKeySet paramSet) {
      Set<ContextKey<?>> notAllowed = Sets.difference(this.params.keySet(), paramSet.allowed());
      if (!notAllowed.isEmpty()) {
         throw new IllegalArgumentException("Parameters not allowed in this parameter set: " + notAllowed);
      } else {
         Set<ContextKey<?>> missingRequired = Sets.difference(paramSet.required(), this.params.keySet());
         if (!missingRequired.isEmpty()) {
            throw new IllegalArgumentException("Missing required parameters: " + missingRequired);
         } else {
            return this.build();
         }
      }
   }
}
