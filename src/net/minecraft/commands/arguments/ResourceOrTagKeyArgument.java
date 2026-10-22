package net.minecraft.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.SharedSuggestionProvider$ElementSuggestionType;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public class ResourceOrTagKeyArgument<T> implements ArgumentType<ResourceOrTagKeyArgument$Result<T>> {
   private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo:bar", "012", "#skeletons", "#minecraft:skeletons");
   private final ResourceKey<? extends Registry<T>> registryKey;

   public ResourceOrTagKeyArgument(final ResourceKey<? extends Registry<T>> registryKey) {
      this.registryKey = registryKey;
   }

   public static <T> ResourceOrTagKeyArgument<T> resourceOrTagKey(final ResourceKey<? extends Registry<T>> key) {
      return new ResourceOrTagKeyArgument<>(key);
   }

   public static <T> ResourceOrTagKeyArgument$Result<T> getResourceOrTagKey(
      final CommandContext<CommandSourceStack> context,
      final String name,
      final ResourceKey<Registry<T>> registryKey,
      final DynamicCommandExceptionType exceptionType
   ) throws CommandSyntaxException {
      ResourceOrTagKeyArgument$Result<?> argument = (ResourceOrTagKeyArgument$Result<?>)context.getArgument(name, ResourceOrTagKeyArgument$Result.class);
      Optional<ResourceOrTagKeyArgument$Result<T>> value = argument.cast(registryKey);
      return value.orElseThrow(() -> exceptionType.create(argument));
   }

   public ResourceOrTagKeyArgument$Result<T> parse(final StringReader reader) throws CommandSyntaxException {
      if (reader.canRead() && reader.peek() == '#') {
         int cursor = reader.getCursor();

         try {
            reader.skip();
            Identifier tagId = Identifier.read(reader);
            return new ResourceOrTagKeyArgument$TagResult<>(TagKey.create(this.registryKey, tagId));
         } catch (CommandSyntaxException var4) {
            reader.setCursor(cursor);
            throw var4;
         }
      } else {
         Identifier resourceId = Identifier.read(reader);
         return new ResourceOrTagKeyArgument$ResourceResult<>(ResourceKey.create(this.registryKey, resourceId));
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
      return SharedSuggestionProvider.listSuggestions(context, builder, this.registryKey, SharedSuggestionProvider$ElementSuggestionType.ALL);
   }

   public Collection<String> getExamples() {
      return EXAMPLES;
   }
}
