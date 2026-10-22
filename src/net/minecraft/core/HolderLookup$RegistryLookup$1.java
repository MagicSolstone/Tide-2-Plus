package net.minecraft.core;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceKey;

class HolderLookup$RegistryLookup$1 implements HolderLookup$RegistryLookup$Delegate<T> {
   HolderLookup$RegistryLookup$1(final HolderLookup$RegistryLookup this$0, final Predicate val$filter) {
      this.val$filter = val$filterx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public HolderLookup$RegistryLookup<T> parent() {
      return this.this$0;
   }

   @Override
   public Optional<Holder$Reference<T>> get(final ResourceKey<T> id) {
      return this.parent().get(id).filter(holder -> filter.test(holder.value()));
   }

   @Override
   public Stream<Holder$Reference<T>> listElements() {
      return this.parent().listElements().filter(e -> filter.test(e.value()));
   }
}
