package net.minecraft.commands.arguments;

import com.mojang.datafixers.util.Either;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

record ResourceOrTagKeyArgument$ResourceResult<T>(ResourceKey<T> key) implements ResourceOrTagKeyArgument$Result<T> {
   @Override
   public Either<ResourceKey<T>, TagKey<T>> unwrap() {
      return Either.left(this.key);
   }

   @Override
   public <E> Optional<ResourceOrTagKeyArgument$Result<E>> cast(final ResourceKey<? extends Registry<E>> registryKey) {
      return this.key.cast(registryKey).map(ResourceOrTagKeyArgument$ResourceResult::new);
   }

   public boolean test(final Holder<T> holder) {
      return holder.is(this.key);
   }

   @Override
   public String asPrintable() {
      return this.key.identifier().toString();
   }
}
