package net.minecraft.core;

import com.google.common.collect.ImmutableMap;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.tags.TagKey;

class MappedRegistry$2 implements HolderLookup$RegistryLookup$Delegate<T> {
   MappedRegistry$2(final MappedRegistry this$0, final ImmutableMap val$pendingTags) {
      this.val$pendingTags = val$pendingTagsx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public HolderLookup$RegistryLookup<T> parent() {
      return this.this$0;
   }

   @Override
   public Optional<HolderSet$Named<T>> get(final TagKey<T> id) {
      return Optional.ofNullable((HolderSet$Named)this.val$pendingTags.get(id));
   }

   @Override
   public Stream<HolderSet$Named<T>> listTags() {
      return this.val$pendingTags.values().stream();
   }
}
