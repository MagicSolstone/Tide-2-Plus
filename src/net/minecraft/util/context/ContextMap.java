package net.minecraft.util.context;

import java.util.Map;
import java.util.NoSuchElementException;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

public final class ContextMap {
   public static final ContextMap EMPTY = new ContextMap(Map.of());
   private final Map<ContextKey<?>, Object> params;

   private ContextMap(final Map<ContextKey<?>, Object> params) {
      this.params = params;
   }

   public static ContextMap$Builder builder() {
      return new ContextMap$Builder();
   }

   public boolean has(final ContextKey<?> key) {
      return this.params.containsKey(key);
   }

   public <T> T getOrThrow(final ContextKey<T> key) {
      T value = this.get(key);
      if (value == null) {
         throw new NoSuchElementException(key.name().toString());
      } else {
         return value;
      }
   }

   @Nullable
   public <T> T get(final ContextKey<T> key) {
      return (T)this.params.get(key);
   }

   @Contract("_,!null->!null; _,_->_")
   @Nullable
   public <T> T getOrDefault(final ContextKey<T> param, @Nullable final T _default) {
      return (T)this.params.getOrDefault(param, _default);
   }
}
