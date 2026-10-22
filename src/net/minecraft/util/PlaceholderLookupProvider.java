package net.minecraft.util;

import com.mojang.serialization.DynamicOps;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderGetter$Provider;
import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.core.HolderSet$Named;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public class PlaceholderLookupProvider implements HolderGetter$Provider {
   private final HolderLookup$Provider context;
   private final PlaceholderLookupProvider$UniversalLookup lookup = new PlaceholderLookupProvider$UniversalLookup(this);
   private final Map<ResourceKey<Object>, Holder$Reference<Object>> holders = new HashMap<>();
   private final Map<TagKey<Object>, HolderSet$Named<Object>> holderSets = new HashMap<>();

   public PlaceholderLookupProvider(final HolderLookup$Provider context) {
      this.context = context;
   }

   @Override
   public <T> Optional<? extends HolderGetter<T>> lookup(final ResourceKey<? extends Registry<? extends T>> key) {
      return Optional.of(this.lookup.castAsLookup());
   }

   public <V> RegistryOps<V> createSerializationContext(final DynamicOps<V> parent) {
      return RegistryOps.create(parent, new PlaceholderLookupProvider$1(this));
   }

   public RegistryContextSwapper createSwapper() {
      return new PlaceholderLookupProvider$2(this);
   }

   public boolean hasRegisteredPlaceholders() {
      return !this.holders.isEmpty() || !this.holderSets.isEmpty();
   }
}
