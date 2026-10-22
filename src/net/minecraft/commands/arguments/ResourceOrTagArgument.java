package net.minecraft.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.SharedSuggestionProvider$ElementSuggestionType;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet$Named;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public class ResourceOrTagArgument<T> implements ArgumentType<ResourceOrTagArgument$Result<T>> {
   private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo:bar", "012", "#skeletons", "#minecraft:skeletons");
   private static final Dynamic2CommandExceptionType ERROR_UNKNOWN_TAG = new Dynamic2CommandExceptionType(
      (id, registry) -> Component.translatableEscape("argument.resource_tag.not_found", id, registry)
   );
   private static final Dynamic3CommandExceptionType ERROR_INVALID_TAG_TYPE = new Dynamic3CommandExceptionType(
      (id, actualRegistry, expectedRegistry) -> Component.translatableEscape("argument.resource_tag.invalid_type", id, actualRegistry, expectedRegistry)
   );
   private final HolderLookup<T> registryLookup;
   private final ResourceKey<? extends Registry<T>> registryKey;

   public ResourceOrTagArgument(final CommandBuildContext context, final ResourceKey<? extends Registry<T>> registryKey) {
      this.registryKey = registryKey;
      this.registryLookup = context.lookupOrThrow(registryKey);
   }

   public static <T> ResourceOrTagArgument<T> resourceOrTag(final CommandBuildContext context, final ResourceKey<? extends Registry<T>> key) {
      return new ResourceOrTagArgument<>(context, key);
   }

   public static <T> ResourceOrTagArgument$Result<T> getResourceOrTag(
      final CommandContext<CommandSourceStack> context, final String name, final ResourceKey<Registry<T>> registryKey
   ) throws CommandSyntaxException {
      ResourceOrTagArgument$Result<?> argument = (ResourceOrTagArgument$Result<?>)context.getArgument(name, ResourceOrTagArgument$Result.class);
      Optional<ResourceOrTagArgument$Result<T>> value = argument.cast(registryKey);
      return value.orElseThrow(() -> (CommandSyntaxException)argument.unwrap().map(element -> {
         ResourceKey<?> elementKey = element.key();
         return ResourceArgument.ERROR_INVALID_RESOURCE_TYPE.create(elementKey.identifier(), elementKey.registry(), registryKey.identifier());
      }, tag -> {
         TagKey<?> tagKey = tag.key();
         return ERROR_INVALID_TAG_TYPE.create(tagKey.location(), tagKey.registry(), registryKey.identifier());
      }));
   }

   public ResourceOrTagArgument$Result<T> parse(final StringReader reader) throws CommandSyntaxException {
      if (reader.canRead() && reader.peek() == '#') {
         int cursor = reader.getCursor();

         try {
            reader.skip();
            Identifier tagId = Identifier.read(reader);
            TagKey<T> tagKey = TagKey.create(this.registryKey, tagId);
            HolderSet$Named<T> holderSet = this.registryLookup
               .get(tagKey)
               .orElseThrow(() -> ERROR_UNKNOWN_TAG.createWithContext(reader, tagId, this.registryKey.identifier()));
            return new ResourceOrTagArgument$TagResult<>(holderSet);
         } catch (CommandSyntaxException var6) {
            reader.setCursor(cursor);
            throw var6;
         }
      } else {
         Identifier resourceId = Identifier.read(reader);
         ResourceKey<T> resourceKey = ResourceKey.create(this.registryKey, resourceId);
         Holder$Reference<T> holder = this.registryLookup
            .get(resourceKey)
            .orElseThrow(() -> ResourceArgument.ERROR_UNKNOWN_RESOURCE.createWithContext(reader, resourceId, this.registryKey.identifier()));
         return new ResourceOrTagArgument$ResourceResult<>(holder);
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
      return SharedSuggestionProvider.listSuggestions(context, builder, this.registryKey, SharedSuggestionProvider$ElementSuggestionType.ALL);
   }

   public Collection<String> getExamples() {
      return EXAMPLES;
   }
}
