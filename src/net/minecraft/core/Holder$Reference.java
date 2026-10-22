package net.minecraft.core;

import com.mojang.datafixers.util.Either;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jspecify.annotations.Nullable;

public non-sealed class Holder$Reference<T> implements Holder<T> {
   private final HolderOwner<T> owner;
   @Nullable
   private Set<TagKey<T>> tags;
   @Nullable
   private DataComponentMap components;
   private final Holder$Reference$Type type;
   @Nullable
   private ResourceKey<T> key;
   @Nullable
   private T value;

   protected Holder$Reference(final Holder$Reference$Type type, final HolderOwner<T> owner, @Nullable final ResourceKey<T> key, @Nullable final T value) {
      this.owner = owner;
      this.type = type;
      this.key = key;
      this.value = value;
   }

   public static <T> Holder$Reference<T> createStandAlone(final HolderOwner<T> owner, final ResourceKey<T> key) {
      return new Holder$Reference<>(Holder$Reference$Type.STAND_ALONE, owner, key, null);
   }

   @Deprecated
   public static <T> Holder$Reference<T> createIntrusive(final HolderOwner<T> owner, @Nullable final T value) {
      return new Holder$Reference<>(Holder$Reference$Type.INTRUSIVE, owner, null, value);
   }

   public ResourceKey<T> key() {
      if (this.key == null) {
         throw new IllegalStateException("Trying to access unbound value '" + this.value + "' from registry " + this.owner);
      } else {
         return this.key;
      }
   }

   @Override
   public T value() {
      if (this.value == null) {
         throw new IllegalStateException("Trying to access unbound value '" + this.key + "' from registry " + this.owner);
      } else {
         return this.value;
      }
   }

   @Override
   public boolean is(final Identifier key) {
      return this.key().identifier().equals(key);
   }

   @Override
   public boolean is(final ResourceKey<T> key) {
      return this.key() == key;
   }

   private Set<TagKey<T>> boundTags() {
      if (this.tags == null) {
         throw new IllegalStateException("Tags not bound");
      } else {
         return this.tags;
      }
   }

   @Override
   public boolean is(final TagKey<T> tag) {
      return this.boundTags().contains(tag);
   }

   @Override
   public boolean is(final Holder<T> holder) {
      return holder.is(this.key());
   }

   @Override
   public boolean is(final Predicate<ResourceKey<T>> predicate) {
      return predicate.test(this.key());
   }

   @Override
   public boolean canSerializeIn(final HolderOwner<T> context) {
      return context.canSerialize(this.owner);
   }

   @Override
   public Either<ResourceKey<T>, T> unwrap() {
      return Either.left(this.key());
   }

   @Override
   public Optional<ResourceKey<T>> unwrapKey() {
      return Optional.of(this.key());
   }

   @Override
   public Holder$Kind kind() {
      return Holder$Kind.REFERENCE;
   }

   @Override
   public boolean isBound() {
      return this.key != null && this.value != null;
   }

   @Override
   public boolean areComponentsBound() {
      return this.components != null;
   }

   void bindKey(final ResourceKey<T> key) {
      if (this.key != null && key != this.key) {
         throw new IllegalStateException("Can't change holder key: existing=" + this.key + ", new=" + key);
      } else {
         this.key = key;
      }
   }

   protected void bindValue(final T value) {
      if (this.type == Holder$Reference$Type.INTRUSIVE && this.value != value) {
         throw new IllegalStateException("Can't change holder " + this.key + " value: existing=" + this.value + ", new=" + value);
      } else {
         this.value = value;
      }
   }

   void bindTags(final Collection<TagKey<T>> tags) {
      this.tags = Set.copyOf(tags);
   }

   public void bindComponents(final DataComponentMap components) {
      this.components = components;
   }

   @Override
   public Stream<TagKey<T>> tags() {
      return this.boundTags().stream();
   }

   @Override
   public DataComponentMap components() {
      return Objects.requireNonNull(this.components, "Components not bound yet");
   }

   @Override
   public String toString() {
      return "Reference{" + this.key + "=" + this.value + "}";
   }
}
