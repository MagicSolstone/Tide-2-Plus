package net.minecraft.commands.arguments;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.core.HolderLookup$RegistryLookup;

public record ResourceOrIdArgument$InlineResult<T, O>(O value) implements ResourceOrIdArgument$Result<T, O> {
   @Override
   public Holder<T> parse(
      final ImmutableStringReader reader,
      final HolderLookup$Provider lookup,
      final DynamicOps<O> ops,
      final Codec<T> codec,
      final HolderLookup$RegistryLookup<T> elementLookup
   ) throws CommandSyntaxException {
      return Holder.direct(
         (T)codec.parse(lookup.createSerializationContext(ops), this.value)
            .getOrThrow(msg -> ResourceOrIdArgument.ERROR_FAILED_TO_PARSE.createWithContext(reader, msg))
      );
   }
}
