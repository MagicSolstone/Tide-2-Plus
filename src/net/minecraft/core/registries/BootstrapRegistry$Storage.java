package net.minecraft.core.registries;

import java.util.Collection;
import java.util.function.Predicate;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.HolderSet$Named;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jspecify.annotations.Nullable;

interface BootstrapRegistry$Storage<T> {
   BootstrapRegistry$Storage<T> freeze();

   void removeIf(Predicate<Holder$Reference<T>> predicate);

   @Nullable
   Holder$Reference<T> get(ResourceKey<T> id);

   Collection<Holder$Reference<T>> listElements();

   @Nullable
   HolderSet$Named<T> get(TagKey<T> id);

   Collection<HolderSet$Named<T>> listTags();
}
