package net.minecraft.core;

import com.mojang.datafixers.util.Either;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.tags.TagKey;
import org.jspecify.annotations.Nullable;

public final class HolderSet$Direct<T> extends HolderSet$ListBacked<T> {
   private static final HolderSet$Direct<?> EMPTY = new HolderSet$Direct(List.of());
   private final List<Holder<T>> contents;
   @Nullable
   private Set<Holder<T>> contentsSet;

   private HolderSet$Direct(final List<Holder<T>> contents) {
      this.contents = contents;
   }

   @Override
   protected List<Holder<T>> contents() {
      return this.contents;
   }

   @Override
   public boolean isBound() {
      return true;
   }

   @Override
   public Either<TagKey<T>, List<Holder<T>>> unwrap() {
      return Either.right(this.contents);
   }

   @Override
   public Optional<TagKey<T>> unwrapKey() {
      return Optional.empty();
   }

   @Override
   public boolean contains(final Holder<T> value) {
      if (this.contentsSet == null) {
         this.contentsSet = Set.copyOf(this.contents);
      }

      return this.contentsSet.contains(value);
   }

   @Override
   public String toString() {
      return "DirectSet[" + this.contents + "]";
   }

   @Override
   public boolean equals(final Object obj) {
      return this == obj ? true : obj instanceof HolderSet$Direct<?> direct && this.contents.equals(direct.contents);
   }

   @Override
   public int hashCode() {
      return this.contents.hashCode();
   }
}
