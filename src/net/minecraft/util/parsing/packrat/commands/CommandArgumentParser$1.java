package net.minecraft.util.parsing.packrat.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

class CommandArgumentParser$1 implements CommandArgumentParser<S> {
   CommandArgumentParser$1(final CommandArgumentParser this$0, final Function val$mapper) {
      this.val$mapper = val$mapperx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public S parseForCommands(final StringReader reader) throws CommandSyntaxException {
      return (S)this.val$mapper.apply(this.this$0.parseForCommands(reader));
   }

   @Override
   public CompletableFuture<Suggestions> parseForSuggestions(final SuggestionsBuilder suggestionsBuilder) {
      return this.this$0.parseForSuggestions(suggestionsBuilder);
   }
}
