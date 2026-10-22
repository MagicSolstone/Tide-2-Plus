package net.minecraft.resources;

import com.mojang.serialization.Lifecycle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.ConcurrentHolderGetter;
import net.minecraft.tags.TagKey;

public abstract class RegistryLoadTask<T> {
   private final Object registryWriteLock = new Object();
   protected final RegistryDataLoader$RegistryData<T> data;
   private final WritableRegistry<T> registry;
   protected final ConcurrentHolderGetter<T> concurrentRegistrationGetter;
   protected final Map<ResourceKey<?>, Exception> loadingErrors;
   private volatile boolean elementsRegistered;

   protected RegistryLoadTask(final RegistryDataLoader$RegistryData<T> data, final Lifecycle lifecycle, final Map<ResourceKey<?>, Exception> loadingErrors) {
      this.data = data;
      this.registry = new MappedRegistry<>(data.key(), lifecycle);
      this.loadingErrors = loadingErrors;
      this.concurrentRegistrationGetter = new ConcurrentHolderGetter<>(this.registryWriteLock, this.registry.createRegistrationLookup());
   }

   protected ResourceKey<? extends Registry<T>> registryKey() {
      return this.registry.key();
   }

   protected Registry<T> readOnlyRegistry() {
      if (!this.elementsRegistered) {
         throw new IllegalStateException("Elements not registered");
      } else {
         return this.registry;
      }
   }

   public abstract CompletableFuture<?> load(RegistryOps$RegistryInfoLookup context, Executor executor);

   protected void registerElements(final Stream<RegistryLoadTask$PendingRegistration<T>> elements) {
      synchronized (this.registryWriteLock) {
         elements.forEach(
            element -> element.value
               .ifLeft(value -> this.registry.register(element.key, (T)value, element.registrationInfo))
               .ifRight(error -> this.loadingErrors.put(element.key, error))
         );
         this.elementsRegistered = true;
      }
   }

   protected void registerTags(final Map<TagKey<T>, List<Holder<T>>> pendingTags) {
      synchronized (this.registryWriteLock) {
         this.registry.bindTags(pendingTags);
      }
   }

   public boolean freezeRegistry(final Map<ResourceKey<?>, Exception> loadingErrors) {
      try {
         this.registry.freeze();
         return true;
      } catch (Exception var3) {
         loadingErrors.put(this.registry.key(), var3);
         return false;
      }
   }

   public Optional<Registry<T>> validateRegistry(final Map<ResourceKey<?>, Exception> loadingErrors) {
      Map<ResourceKey<?>, Exception> registryErrors = new HashMap<>();
      this.data.validator().validate(this.registry, registryErrors);
      if (registryErrors.isEmpty()) {
         return Optional.of(this.registry);
      } else {
         loadingErrors.putAll(registryErrors);
         return Optional.empty();
      }
   }
}
