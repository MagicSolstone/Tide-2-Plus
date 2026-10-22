package net.minecraft.commands.arguments;

import com.mojang.datafixers.util.Either;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.HolderSet$Named;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface ResourceOrTagArgument$Result<T> extends Predicate<Holder<T>> {
   Either<Holder$Reference<T>, HolderSet$Named<T>> unwrap();

   <E> Optional<ResourceOrTagArgument$Result<E>> cast(final ResourceKey<? extends Registry<E>> registryKey);

   String asPrintable();
}
