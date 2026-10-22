package net.minecraft.commands.arguments;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.core.HolderLookup$RegistryLookup;

public sealed interface ResourceOrIdArgument$Result<T, O> permits ResourceOrIdArgument$InlineResult, ResourceOrIdArgument$ReferenceResult {
   Holder<T> parse(ImmutableStringReader reader, HolderLookup$Provider lookup, DynamicOps<O> ops, Codec<T> codec, HolderLookup$RegistryLookup<T> elementLookup) throws CommandSyntaxException;
}
