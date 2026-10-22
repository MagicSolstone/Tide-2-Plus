package net.minecraft.core.registries;

import com.mojang.serialization.Lifecycle;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.HolderLookup$RegistryLookup;
import net.minecraft.core.HolderSet$Named;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public class BootstrapRegistry<T> implements HolderLookup$RegistryLookup<T> {
   private final ResourceKey<? extends Registry<T>> key;
   private final Lifecycle lifecycle;
   private BootstrapRegistry$Storage<T> storage = new BootstrapRegistry$RegistrationStorage(this);

   public BootstrapRegistry(final ResourceKey<? extends Registry<T>> key, final Lifecycle lifecycle) {
      this.key = key;
      this.lifecycle = lifecycle;
   }

   @Override
   public ResourceKey<? extends Registry<? extends T>> key() {
      return this.key;
   }

   @Override
   public Lifecycle registryLifecycle() {
      return this.lifecycle;
   }

   public void freeze() {
      this.storage = this.storage.freeze();
   }

   public void removeIf(final Predicate<Holder$Reference<T>> predicate) {
      this.storage.removeIf(predicate);
   }

   @Override
   public Optional<Holder$Reference<T>> get(final ResourceKey<T> id) {
      return Optional.ofNullable(this.storage.get(id));
   }

   @Override
   public Stream<Holder$Reference<T>> listElements() {
      return this.storage.listElements().stream();
   }

   @Override
   public Optional<HolderSet$Named<T>> get(final TagKey<T> id) {
      return Optional.ofNullable(this.storage.get(id));
   }

   @Override
   public Stream<HolderSet$Named<T>> listTags() {
      return this.storage.listTags().stream();
   }
}
