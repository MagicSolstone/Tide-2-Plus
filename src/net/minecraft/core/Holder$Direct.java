package net.minecraft.core;

import com.mojang.datafixers.util.Either;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public record Holder$Direct<T>(T value, DataComponentMap components) implements Holder<T> {
   @Override
   public boolean isBound() {
      return true;
   }

   @Override
   public boolean areComponentsBound() {
      return true;
   }

   @Override
   public boolean is(final Identifier key) {
      return false;
   }

   @Override
   public boolean is(final ResourceKey<T> key) {
      return false;
   }

   @Override
   public boolean is(final TagKey<T> tag) {
      return false;
   }

   @Override
   public boolean is(final Holder<T> holder) {
      return this.value.equals(holder.value());
   }

   @Override
   public boolean is(final Predicate<ResourceKey<T>> predicate) {
      return false;
   }

   @Override
   public Either<ResourceKey<T>, T> unwrap() {
      return Either.right(this.value);
   }

   @Override
   public Optional<ResourceKey<T>> unwrapKey() {
      return Optional.empty();
   }

   @Override
   public Holder$Kind kind() {
      return Holder$Kind.DIRECT;
   }

   @Override
   public String toString() {
      return "Direct{" + this.value + "}";
   }

   @Override
   public boolean canSerializeIn(final HolderOwner<T> registry) {
      return true;
   }

   @Override
   public Stream<TagKey<T>> tags() {
      return Stream.of();
   }
}
