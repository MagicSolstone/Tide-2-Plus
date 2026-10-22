package net.minecraft.core;

import java.util.Objects;
import java.util.Optional;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

class MappedRegistry$1 implements HolderGetter<T> {
   MappedRegistry$1(final MappedRegistry this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public Optional<Holder$Reference<T>> get(final ResourceKey<T> id) {
      return Optional.of(this.getOrThrow(id));
   }

   @Override
   public Holder$Reference<T> getOrThrow(final ResourceKey<T> id) {
      return this.this$0.getOrCreateHolderOrThrow(id);
   }

   @Override
   public Optional<HolderSet$Named<T>> get(final TagKey<T> id) {
      return Optional.of(this.getOrThrow(id));
   }

   @Override
   public HolderSet$Named<T> getOrThrow(final TagKey<T> id) {
      return this.this$0.getOrCreateTagForRegistration(id);
   }

   @Override
   public boolean canSerialize(final HolderOwner<T> owner) {
      return this.this$0.canSerialize(owner);
   }
}
