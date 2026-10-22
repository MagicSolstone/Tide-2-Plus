package net.minecraft.commands.arguments;

import com.mojang.datafixers.util.Either;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public interface ResourceOrTagKeyArgument$Result<T> extends Predicate<Holder<T>> {
   Either<ResourceKey<T>, TagKey<T>> unwrap();

   <E> Optional<ResourceOrTagKeyArgument$Result<E>> cast(final ResourceKey<? extends Registry<E>> registryKey);

   String asPrintable();
}
