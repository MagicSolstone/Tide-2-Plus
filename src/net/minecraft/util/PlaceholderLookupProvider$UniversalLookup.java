package net.minecraft.util;

import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.HolderSet$Named;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

class PlaceholderLookupProvider$UniversalLookup implements HolderGetter<Object> {
   private PlaceholderLookupProvider$UniversalLookup(final PlaceholderLookupProvider this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
   }

   @Override
   public Optional<Holder$Reference<Object>> get(final ResourceKey<Object> id) {
      return Optional.of(this.getOrCreate(id));
   }

   @Override
   public Holder$Reference<Object> getOrThrow(final ResourceKey<Object> id) {
      return this.getOrCreate(id);
   }

   private Holder$Reference<Object> getOrCreate(final ResourceKey<Object> id) {
      return this.this$0.holders.computeIfAbsent(id, k -> Holder$Reference.createStandAlone(this, (ResourceKey<Object>)k));
   }

   @Override
   public Optional<HolderSet$Named<Object>> get(final TagKey<Object> id) {
      return Optional.of(this.getOrCreate(id));
   }

   @Override
   public HolderSet$Named<Object> getOrThrow(final TagKey<Object> id) {
      return this.getOrCreate(id);
   }

   private HolderSet$Named<Object> getOrCreate(final TagKey<Object> id) {
      return this.this$0.holderSets.computeIfAbsent(id, k -> HolderSet.emptyNamed(this, (TagKey<Object>)k));
   }

   public <T> HolderGetter<T> castAsLookup() {
      return this;
   }
}
