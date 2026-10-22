package net.minecraft.core;

import com.mojang.datafixers.util.Either;
import java.util.List;
import java.util.Optional;
import net.minecraft.tags.TagKey;
import org.jspecify.annotations.Nullable;

public class HolderSet$Named<T> extends HolderSet$ListBacked<T> {
   private final HolderOwner<T> owner;
   private final TagKey<T> key;
   @Nullable
   private List<Holder<T>> contents;

   HolderSet$Named(final HolderOwner<T> owner, final TagKey<T> key) {
      this.owner = owner;
      this.key = key;
   }

   void bind(final List<Holder<T>> contents) {
      this.contents = List.copyOf(contents);
   }

   public TagKey<T> key() {
      return this.key;
   }

   @Override
   protected List<Holder<T>> contents() {
      if (this.contents == null) {
         throw new IllegalStateException("Trying to access unbound tag '" + this.key + "' from registry " + this.owner);
      } else {
         return this.contents;
      }
   }

   @Override
   public boolean isBound() {
      return this.contents != null;
   }

   @Override
   public Either<TagKey<T>, List<Holder<T>>> unwrap() {
      return Either.left(this.key);
   }

   @Override
   public Optional<TagKey<T>> unwrapKey() {
      return Optional.of(this.key);
   }

   @Override
   public boolean contains(final Holder<T> value) {
      return value.is(this.key);
   }

   @Override
   public String toString() {
      return "NamedSet(" + this.key + ")[" + this.contents + "]";
   }

   @Override
   public boolean canSerializeIn(final HolderOwner<T> context) {
      return context.canSerialize(this.owner);
   }
}
