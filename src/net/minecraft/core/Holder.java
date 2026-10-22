package net.minecraft.core;

import com.mojang.datafixers.util.Either;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public sealed interface Holder<T> permits Holder$Direct, Holder$Reference {
   T value();

   boolean isBound();

   boolean areComponentsBound();

   boolean is(Identifier key);

   boolean is(ResourceKey<T> key);

   boolean is(Predicate<ResourceKey<T>> predicate);

   boolean is(TagKey<T> tag);

   @Deprecated
   boolean is(Holder<T> holder);

   Stream<TagKey<T>> tags();

   DataComponentMap components();

   Either<ResourceKey<T>, T> unwrap();

   Optional<ResourceKey<T>> unwrapKey();

   Holder$Kind kind();

   boolean canSerializeIn(HolderOwner<T> registry);

   default Optional<String> getRegisteredNameIfPresent() {
      return this.unwrapKey().map(key -> key.identifier().toString());
   }

   default String getRegisteredName() {
      return this.getRegisteredNameIfPresent().orElse("[unregistered]");
   }

   static <T> Holder<T> direct(final T value) {
      return new Holder$Direct<>(value, DataComponentMap.EMPTY);
   }

   static <T> Holder<T> direct(final T value, final DataComponentMap components) {
      return new Holder$Direct<>(value, components);
   }
}
