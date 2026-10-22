package net.minecraft.core.registries;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.HolderSet$Named;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jspecify.annotations.Nullable;

class BootstrapRegistry$FrozenStorage<T> implements BootstrapRegistry$Storage<T> {
   private final Map<ResourceKey<T>, Holder$Reference<T>> holders;
   private final Map<TagKey<T>, HolderSet$Named<T>> holderSets;

   private BootstrapRegistry$FrozenStorage(final Map<ResourceKey<T>, Holder$Reference<T>> holders, final Map<TagKey<T>, HolderSet$Named<T>> holderSets) {
      this.holders = Map.copyOf(holders);
      this.holderSets = Map.copyOf(holderSets);
   }

   @Override
   public BootstrapRegistry$Storage<T> freeze() {
      return this;
   }

   @Override
   public void removeIf(final Predicate<Holder$Reference<T>> predicate) {
      throw new UnsupportedOperationException("Registry is already frozen");
   }

   @Nullable
   @Override
   public Holder$Reference<T> get(final ResourceKey<T> id) {
      return this.holders.get(id);
   }

   @Override
   public Collection<Holder$Reference<T>> listElements() {
      return this.holders.values();
   }

   @Nullable
   @Override
   public HolderSet$Named<T> get(final TagKey<T> id) {
      return this.holderSets.get(id);
   }

   @Override
   public Collection<HolderSet$Named<T>> listTags() {
      return this.holderSets.values();
   }
}
