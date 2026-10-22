package net.minecraft.util.parsing.packrat.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

class CommandArgumentParser$2 implements CommandArgumentParser<T> {
   CommandArgumentParser$2(
      final CommandArgumentParser this$0,
      final CommandArgumentParser val$valueParser,
      final Codec val$codec,
      final DynamicOps val$ops,
      final DynamicCommandExceptionType val$exceptionType
   ) {
      this.val$valueParser = val$valueParserx;
      this.val$codec = val$codecx;
      this.val$ops = val$opsx;
      this.val$exceptionType = val$exceptionTypex;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public T parseForCommands(final StringReader reader) throws CommandSyntaxException {
      int cursor = reader.getCursor();
      O tag = (O)this.val$valueParser.parseForCommands(reader);
      DataResult<T> result = this.val$codec.parse(this.val$ops, tag);
      return (T)result.getOrThrow(message -> {
         reader.setCursor(cursor);
         return exceptionType.createWithContext(reader, message);
      });
   }

   @Override
   public CompletableFuture<Suggestions> parseForSuggestions(final SuggestionsBuilder suggestionsBuilder) {
      return this.this$0.parseForSuggestions(suggestionsBuilder);
   }
}
