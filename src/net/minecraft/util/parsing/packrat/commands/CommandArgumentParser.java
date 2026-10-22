package net.minecraft.util.parsing.packrat.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface CommandArgumentParser<T> {
   T parseForCommands(StringReader reader) throws CommandSyntaxException;

   CompletableFuture<Suggestions> parseForSuggestions(SuggestionsBuilder suggestionsBuilder);

   default <S> CommandArgumentParser<S> mapResult(final Function<T, S> mapper) {
      return new CommandArgumentParser$1(this, mapper);
   }

   default <T, O> CommandArgumentParser<T> withCodec(
      final DynamicOps<O> ops, final CommandArgumentParser<O> valueParser, final Codec<T> codec, final DynamicCommandExceptionType exceptionType
   ) {
      return new CommandArgumentParser$2(this, valueParser, codec, ops, exceptionType);
   }
}
