package net.minecraft.commands.arguments.item;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

class ItemParser$SuggestionsVisitor implements ItemParser$Visitor {
   private Function<SuggestionsBuilder, CompletableFuture<Suggestions>> suggestions = ItemParser.SUGGEST_NOTHING;

   private ItemParser$SuggestionsVisitor() {
   }

   @Override
   public void visitSuggestions(final Function<SuggestionsBuilder, CompletableFuture<Suggestions>> suggestions) {
      this.suggestions = suggestions;
   }

   public CompletableFuture<Suggestions> resolveSuggestions(final SuggestionsBuilder builder, final StringReader reader) {
      return this.suggestions.apply(builder.createOffset(reader.getCursor()));
   }
}
