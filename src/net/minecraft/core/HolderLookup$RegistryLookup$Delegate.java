package net.minecraft.core;

import com.mojang.serialization.Lifecycle;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public interface HolderLookup$RegistryLookup$Delegate<T> extends HolderLookup$RegistryLookup<T> {
   HolderLookup$RegistryLookup<T> parent();

   @Override
   default ResourceKey<? extends Registry<? extends T>> key() {
      return this.parent().key();
   }

   @Override
   default Lifecycle registryLifecycle() {
      return this.parent().registryLifecycle();
   }

   @Override
   default Optional<Holder$Reference<T>> get(final ResourceKey<T> id) {
      return this.parent().get(id);
   }

   @Override
   default Stream<Holder$Reference<T>> listElements() {
      return this.parent().listElements();
   }

   @Override
   default Optional<HolderSet$Named<T>> get(final TagKey<T> id) {
      return this.parent().get(id);
   }

   @Override
   default Stream<HolderSet$Named<T>> listTags() {
      return this.parent().listTags();
   }

   @Override
   default boolean canSerialize(final HolderOwner<T> owner) {
      return this.parent().canSerialize(owner);
   }
}
