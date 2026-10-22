package net.minecraft.core;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.resources.ResourceKey;

class MappedRegistry$3 implements Registry$PendingTags<T> {
   MappedRegistry$3(
      final MappedRegistry this$0, final Map val$pendingContents, final HolderLookup$RegistryLookup val$patchedHolder, final ImmutableMap val$pendingTags
   ) {
      this.val$pendingContents = val$pendingContentsx;
      this.val$patchedHolder = val$patchedHolderx;
      this.val$pendingTags = val$pendingTagsx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public ResourceKey<? extends Registry<? extends T>> key() {
      return this.this$0.key();
   }

   @Override
   public int size() {
      return this.val$pendingContents.size();
   }

   @Override
   public HolderLookup$RegistryLookup<T> lookup() {
      return this.val$patchedHolder;
   }

   @Override
   public void apply() {
      this.val$pendingTags.forEach((id, tag) -> {
         List<Holder<T>> values = pendingContents.getOrDefault(id, List.of());
         tag.bind(values);
      });
      this.this$0.allTags = MappedRegistry$TagSet.fromMap(this.val$pendingTags);
      this.this$0.refreshTagsInHolders();
   }
}
