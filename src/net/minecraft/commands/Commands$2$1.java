package net.minecraft.commands;

import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.HolderLookup$RegistryLookup;
import net.minecraft.core.HolderLookup$RegistryLookup$Delegate;
import net.minecraft.core.HolderSet;
import net.minecraft.core.HolderSet$Named;
import net.minecraft.tags.TagKey;

class Commands$2$1 implements HolderLookup$RegistryLookup$Delegate<T> {
   Commands$2$1(final Commands$2 this$0, final HolderLookup$RegistryLookup val$original) {
      this.val$original = val$originalx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public HolderLookup$RegistryLookup<T> parent() {
      return this.val$original;
   }

   @Override
   public Optional<HolderSet$Named<T>> get(final TagKey<T> id) {
      return Optional.of(this.getOrThrow(id));
   }

   @Override
   public HolderSet$Named<T> getOrThrow(final TagKey<T> id) {
      Optional<HolderSet$Named<T>> tag = this.parent().get(id);
      return (HolderSet$Named<T>)tag.orElseGet(() -> HolderSet.emptyNamed(this.parent(), id));
   }
}
