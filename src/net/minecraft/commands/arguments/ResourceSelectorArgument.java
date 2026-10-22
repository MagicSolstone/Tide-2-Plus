package net.minecraft.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.SharedSuggestionProvider$ElementSuggestionType;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.apache.commons.io.FilenameUtils;

public class ResourceSelectorArgument<T> implements ArgumentType<Collection<Holder$Reference<T>>> {
   private static final Collection<String> EXAMPLES = List.of("minecraft:*", "*:asset", "*");
   public static final Dynamic2CommandExceptionType ERROR_NO_MATCHES = new Dynamic2CommandExceptionType(
      (selector, registry) -> Component.translatableEscape("argument.resource_selector.not_found", selector, registry)
   );
   private final ResourceKey<? extends Registry<T>> registryKey;
   private final HolderLookup<T> registryLookup;

   private ResourceSelectorArgument(final CommandBuildContext context, final ResourceKey<? extends Registry<T>> registryKey) {
      this.registryKey = registryKey;
      this.registryLookup = context.lookupOrThrow(registryKey);
   }

   public Collection<Holder$Reference<T>> parse(final StringReader reader) throws CommandSyntaxException {
      String pattern = ensureNamespaced(readPattern(reader));
      List<Holder$Reference<T>> results = this.registryLookup.listElements().filter(element -> matches(pattern, element.key().identifier())).toList();
      if (results.isEmpty()) {
         throw ERROR_NO_MATCHES.createWithContext(reader, pattern, this.registryKey.identifier());
      } else {
         return results;
      }
   }

   public static <T> Collection<Holder$Reference<T>> parse(final StringReader reader, final HolderLookup<T> registry) {
      String pattern = ensureNamespaced(readPattern(reader));
      return registry.listElements().filter(element -> matches(pattern, element.key().identifier())).toList();
   }

   private static String readPattern(final StringReader reader) {
      int start = reader.getCursor();

      while (reader.canRead() && isAllowedPatternCharacter(reader.peek())) {
         reader.skip();
      }

      return reader.getString().substring(start, reader.getCursor());
   }

   private static boolean isAllowedPatternCharacter(final char character) {
      return Identifier.isAllowedInIdentifier(character) || character == '*' || character == '?';
   }

   private static String ensureNamespaced(final String input) {
      return !input.contains(":") ? "minecraft:" + input : input;
   }

   private static boolean matches(final String pattern, final Identifier key) {
      return FilenameUtils.wildcardMatch(key.toString(), pattern);
   }

   public static <T> ResourceSelectorArgument<T> resourceSelector(final CommandBuildContext context, final ResourceKey<? extends Registry<T>> registry) {
      return new ResourceSelectorArgument<>(context, registry);
   }

   public static <T> Collection<Holder$Reference<T>> getSelectedResources(final CommandContext<CommandSourceStack> context, final String name) {
      return (Collection<Holder$Reference<T>>)context.getArgument(name, Collection.class);
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
      return SharedSuggestionProvider.listSuggestions(context, builder, this.registryKey, SharedSuggestionProvider$ElementSuggestionType.ELEMENTS);
   }

   public Collection<String> getExamples() {
      return EXAMPLES;
   }
}
