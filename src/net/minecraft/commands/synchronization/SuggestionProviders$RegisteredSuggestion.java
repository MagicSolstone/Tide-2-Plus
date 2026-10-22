package net.minecraft.commands.synchronization;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.resources.Identifier;

record SuggestionProviders$RegisteredSuggestion(Identifier name, SuggestionProvider<SharedSuggestionProvider> delegate)
   implements SuggestionProvider<SharedSuggestionProvider> {
   public CompletableFuture<Suggestions> getSuggestions(final CommandContext<SharedSuggestionProvider> context, final SuggestionsBuilder builder) throws CommandSyntaxException {
      return this.delegate.getSuggestions(context, builder);
   }
}
