package net.minecraft.core.registries;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.HolderSet;
import net.minecraft.core.HolderSet$Named;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

class BootstrapRegistry$RegistrationStorage implements BootstrapRegistry$Storage<T> {
   private final Map<ResourceKey<T>, Holder$Reference<T>> holders;
   private final Map<TagKey<T>, HolderSet$Named<T>> holderSets;

   private BootstrapRegistry$RegistrationStorage(final BootstrapRegistry this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.holders = new HashMap<>();
      this.holderSets = new HashMap<>();
   }

   @Override
   public BootstrapRegistry$Storage<T> freeze() {
      return new BootstrapRegistry$FrozenStorage(this.holders, this.holderSets);
   }

   @Override
   public void removeIf(final Predicate<Holder$Reference<T>> predicate) {
      this.holders.values().removeIf(predicate);
   }

   @Override
   public Holder$Reference<T> get(final ResourceKey<T> id) {
      return (Holder$Reference<T>)this.holders.computeIfAbsent(id, key -> Holder$Reference.createStandAlone(this.this$0, key));
   }

   @Override
   public Collection<Holder$Reference<T>> listElements() {
      throw new UnsupportedOperationException("List is not available during bootstrap");
   }

   @Override
   public HolderSet$Named<T> get(final TagKey<T> id) {
      return (HolderSet$Named<T>)this.holderSets.computeIfAbsent(id, key -> HolderSet.emptyNamed(this.this$0, key));
   }

   @Override
   public Collection<HolderSet$Named<T>> listTags() {
      throw new UnsupportedOperationException("List is not available during bootstrap");
   }
}
