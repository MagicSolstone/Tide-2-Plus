package net.minecraft.resources;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.core.Registry;
import net.minecraft.util.ExtraCodecs;

public class RegistryOps<T> extends DelegatingOps<T> {
   private final RegistryOps$RegistryInfoLookup lookupProvider;

   public static <T> RegistryOps<T> create(final DynamicOps<T> parent, final HolderLookup$Provider lookupProvider) {
      return create(parent, new RegistryOps$HolderLookupAdapter(lookupProvider));
   }

   public static <T> RegistryOps<T> create(final DynamicOps<T> parent, final RegistryOps$RegistryInfoLookup lookupProvider) {
      return new RegistryOps<>(parent, lookupProvider);
   }

   public static <T> Dynamic<T> injectRegistryContext(final Dynamic<T> dynamic, final HolderLookup$Provider lookupProvider) {
      return new Dynamic(lookupProvider.createSerializationContext(dynamic.getOps()), dynamic.getValue());
   }

   private RegistryOps(final DynamicOps<T> parent, final RegistryOps$RegistryInfoLookup lookupProvider) {
      super(parent);
      this.lookupProvider = lookupProvider;
   }

   public <U> RegistryOps<U> withParent(final DynamicOps<U> parent) {
      return (RegistryOps<U>)(parent == this.delegate ? this : new RegistryOps((DynamicOps<T>)parent, this.lookupProvider));
   }

   public <E> Optional<HolderGetter<E>> getter(final ResourceKey<? extends Registry<? extends E>> registryKey) {
      return this.lookupProvider.lookup(registryKey);
   }

   @Override
   public boolean equals(final Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && this.getClass() == obj.getClass()) {
         RegistryOps<?> ops = (RegistryOps<?>)obj;
         return this.delegate.equals(ops.delegate) && this.lookupProvider.equals(ops.lookupProvider);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return this.delegate.hashCode() * 31 + this.lookupProvider.hashCode();
   }

   public static <E, O> RecordCodecBuilder<O, HolderGetter<E>> retrieveGetter(final ResourceKey<? extends Registry<? extends E>> registryKey) {
      return ExtraCodecs.retrieveContext(
            ops -> ops instanceof RegistryOps<?> registryOps
               ? registryOps.lookupProvider
                  .lookup(registryKey)
                  .map(r -> DataResult.success(r, Lifecycle.stable()))
                  .orElseGet(() -> DataResult.error(() -> "Unknown registry: " + registryKey))
               : DataResult.error(() -> "Not a registry ops")
         )
         .forGetter(var0 -> null);
   }

   public static <E, O> RecordCodecBuilder<O, Holder$Reference<E>> retrieveElement(final ResourceKey<E> key) {
      ResourceKey<? extends Registry<E>> registryKey = ResourceKey.createRegistryKey(key.registry());
      return ExtraCodecs.retrieveContext(
            ops -> ops instanceof RegistryOps<?> registryOps
               ? registryOps.lookupProvider
                  .lookup(registryKey)
                  .flatMap(r -> r.get(key))
                  .<DataResult<E>>map(DataResult::success)
                  .orElseGet(() -> DataResult.error(() -> "Can't find value: " + key))
               : DataResult.error(() -> "Not a registry ops")
         )
         .forGetter(var0 -> null);
   }
}
