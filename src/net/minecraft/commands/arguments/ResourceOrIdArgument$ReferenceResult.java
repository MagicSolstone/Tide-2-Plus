package net.minecraft.commands.arguments;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.core.HolderLookup$RegistryLookup;
import net.minecraft.resources.ResourceKey;

public record ResourceOrIdArgument$ReferenceResult<T, O>(ResourceKey<T> key) implements ResourceOrIdArgument$Result<T, O> {
   @Override
   public Holder<T> parse(
      final ImmutableStringReader reader,
      final HolderLookup$Provider lookup,
      final DynamicOps<O> ops,
      final Codec<T> codec,
      final HolderLookup$RegistryLookup<T> elementLookup
   ) throws CommandSyntaxException {
      return elementLookup.get(this.key)
         .orElseThrow(() -> ResourceOrIdArgument.ERROR_NO_SUCH_ELEMENT.createWithContext(reader, this.key.identifier(), this.key.registry()));
   }
}
