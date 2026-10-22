package net.minecraft.commands.arguments;

import com.mojang.datafixers.util.Either;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.HolderSet$Named;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

record ResourceOrTagArgument$TagResult<T>(HolderSet$Named<T> tag) implements ResourceOrTagArgument$Result<T> {
   @Override
   public Either<Holder$Reference<T>, HolderSet$Named<T>> unwrap() {
      return Either.right(this.tag);
   }

   @Override
   public <E> Optional<ResourceOrTagArgument$Result<E>> cast(final ResourceKey<? extends Registry<E>> registryKey) {
      return this.tag.key().isFor(registryKey) ? Optional.of((ResourceOrTagArgument$Result<E>)this) : Optional.empty();
   }

   public boolean test(final Holder<T> holder) {
      return this.tag.contains(holder);
   }

   @Override
   public String asPrintable() {
      return "#" + this.tag.key().location();
   }
}
