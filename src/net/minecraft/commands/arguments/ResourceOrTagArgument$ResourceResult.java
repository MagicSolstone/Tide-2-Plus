package net.minecraft.commands.arguments;

import com.mojang.datafixers.util.Either;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.HolderSet$Named;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

record ResourceOrTagArgument$ResourceResult<T>(Holder$Reference<T> value) implements ResourceOrTagArgument$Result<T> {
   @Override
   public Either<Holder$Reference<T>, HolderSet$Named<T>> unwrap() {
      return Either.left(this.value);
   }

   @Override
   public <E> Optional<ResourceOrTagArgument$Result<E>> cast(final ResourceKey<? extends Registry<E>> registryKey) {
      return this.value.key().isFor(registryKey) ? Optional.of((ResourceOrTagArgument$Result<E>)this) : Optional.empty();
   }

   public boolean test(final Holder<T> holder) {
      return holder.equals(this.value);
   }

   @Override
   public String asPrintable() {
      return this.value.key().identifier().toString();
   }
}
