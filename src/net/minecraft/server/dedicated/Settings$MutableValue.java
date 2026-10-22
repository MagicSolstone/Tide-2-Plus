package net.minecraft.server.dedicated;

import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.RegistryAccess;

public class Settings$MutableValue<V> implements Supplier<V> {
   private final String key;
   private final V value;
   private final Function<V, String> serializer;

   private Settings$MutableValue(final String this$0, final V key, final Function<V, String> value, final Function serializer) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.key = key;
      this.value = value;
      this.serializer = serializer;
   }

   @Override
   public V get() {
      return this.value;
   }

   public T update(final RegistryAccess registryAccess, final V value) {
      Properties properties = this.this$0.cloneProperties();
      properties.put(this.key, this.serializer.apply(value));
      return (T)this.this$0.reload(registryAccess, properties);
   }
}
