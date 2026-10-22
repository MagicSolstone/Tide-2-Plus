package net.minecraft.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.SharedSuggestionProvider$ElementSuggestionType;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.core.HolderLookup$RegistryLookup;
import net.minecraft.core.Registry;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.SnbtGrammar;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dialog.Dialog;
import net.minecraft.util.parsing.packrat.Atom;
import net.minecraft.util.parsing.packrat.Dictionary;
import net.minecraft.util.parsing.packrat.NamedRule;
import net.minecraft.util.parsing.packrat.Term;
import net.minecraft.util.parsing.packrat.commands.Grammar;
import net.minecraft.util.parsing.packrat.commands.IdentifierParseRule;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import org.jspecify.annotations.Nullable;

public class ResourceOrIdArgument<T> implements ArgumentType<Holder<T>> {
   static final Collection<String> EXAMPLES = List.of("foo", "foo:bar", "012", "{}", "true");
   public static final DynamicCommandExceptionType ERROR_FAILED_TO_PARSE = new DynamicCommandExceptionType(
      error -> Component.translatableEscape("argument.resource_or_id.failed_to_parse", error)
   );
   public static final Dynamic2CommandExceptionType ERROR_NO_SUCH_ELEMENT = new Dynamic2CommandExceptionType(
      (id, registry) -> Component.translatableEscape("argument.resource_or_id.no_such_element", id, registry)
   );
   public static final DynamicOps<Tag> OPS = NbtOps.INSTANCE;
   private final HolderLookup$Provider registryLookup;
   private final Optional<? extends HolderLookup$RegistryLookup<T>> elementLookup;
   private final Codec<T> codec;
   private final Grammar<ResourceOrIdArgument$Result<T, Tag>> grammar;
   private final ResourceKey<? extends Registry<T>> registryKey;

   protected ResourceOrIdArgument(final CommandBuildContext context, final ResourceKey<? extends Registry<T>> registryKey, final Codec<T> codec) {
      this.registryLookup = context;
      this.elementLookup = context.lookup(registryKey);
      this.registryKey = registryKey;
      this.codec = codec;
      this.grammar = createGrammar(registryKey, OPS);
   }

   public static <T, O> Grammar<ResourceOrIdArgument$Result<T, O>> createGrammar(final ResourceKey<? extends Registry<T>> registryKey, final DynamicOps<O> ops) {
      Grammar<O> inlineValueGrammar = SnbtGrammar.createParser((DynamicOps<T>)ops);
      Dictionary<StringReader> rules = new Dictionary<>();
      Atom<ResourceOrIdArgument$Result<T, O>> result = Atom.of("result");
      Atom<Identifier> id = Atom.of("id");
      Atom<O> value = Atom.of("value");
      rules.put(id, IdentifierParseRule.INSTANCE);
      rules.put(value, inlineValueGrammar.top().value());
      NamedRule<StringReader, ResourceOrIdArgument$Result<T, O>> topRule = rules.put(result, Term.alternative(rules.named(id), rules.named(value)), scope -> {
         Identifier parsedId = scope.get(id);
         if (parsedId != null) {
            return new ResourceOrIdArgument$ReferenceResult<>(ResourceKey.create(registryKey, parsedId));
         } else {
            O parsedInline = scope.getOrThrow(value);
            return new ResourceOrIdArgument$InlineResult<>(parsedInline);
         }
      });
      return new Grammar<>(rules, topRule);
   }

   public static ResourceOrIdArgument$LootTableArgument lootTable(final CommandBuildContext context) {
      return new ResourceOrIdArgument$LootTableArgument(context);
   }

   public static Holder<LootTable> getLootTable(final CommandContext<CommandSourceStack> context, final String name) throws CommandSyntaxException {
      return getResource(context, name);
   }

   public static ResourceOrIdArgument$LootModifierArgument lootModifier(final CommandBuildContext context) {
      return new ResourceOrIdArgument$LootModifierArgument(context);
   }

   public static Holder<LootItemFunction> getLootModifier(final CommandContext<CommandSourceStack> context, final String name) {
      return getResource(context, name);
   }

   public static ResourceOrIdArgument$LootPredicateArgument lootPredicate(final CommandBuildContext context) {
      return new ResourceOrIdArgument$LootPredicateArgument(context);
   }

   public static Holder<LootItemCondition> getLootPredicate(final CommandContext<CommandSourceStack> context, final String name) {
      return getResource(context, name);
   }

   public static ResourceOrIdArgument$ContextFloatProviderArgument floatProvider(final CommandBuildContext context) {
      return new ResourceOrIdArgument$ContextFloatProviderArgument(context);
   }

   public static Holder<ContextFloatProvider> getFloatProvider(final CommandContext<CommandSourceStack> context, final String name) {
      return getResource(context, name);
   }

   public static ResourceOrIdArgument$ContextIntProviderArgument intProvider(final CommandBuildContext context) {
      return new ResourceOrIdArgument$ContextIntProviderArgument(context);
   }

   public static Holder<ContextIntProvider> getIntProvider(final CommandContext<CommandSourceStack> context, final String name) {
      return getResource(context, name);
   }

   public static ResourceOrIdArgument$FeatureArgument feature(final CommandBuildContext context) {
      return new ResourceOrIdArgument$FeatureArgument(context);
   }

   public static Holder<Feature> getFeature(final CommandContext<CommandSourceStack> context, final String name) throws CommandSyntaxException {
      return getResource(context, name);
   }

   public static ResourceOrIdArgument$DialogArgument dialog(final CommandBuildContext context) {
      return new ResourceOrIdArgument$DialogArgument(context);
   }

   public static Holder<Dialog> getDialog(final CommandContext<CommandSourceStack> context, final String name) {
      return getResource(context, name);
   }

   private static <T> Holder<T> getResource(final CommandContext<CommandSourceStack> context, final String name) {
      return (Holder<T>)context.getArgument(name, Holder.class);
   }

   @Nullable
   public Holder<T> parse(final StringReader reader) throws CommandSyntaxException {
      return this.parse(reader, this.grammar, OPS);
   }

   @Nullable
   private <O> Holder<T> parse(final StringReader reader, final Grammar<ResourceOrIdArgument$Result<T, O>> grammar, final DynamicOps<O> ops) throws CommandSyntaxException {
      ResourceOrIdArgument$Result<T, O> contents = grammar.parseForCommands(reader);
      return this.elementLookup.isEmpty()
         ? null
         : contents.parse(reader, this.registryLookup, ops, this.codec, (HolderLookup$RegistryLookup<T>)this.elementLookup.get());
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
      return SharedSuggestionProvider.listSuggestions(context, builder, this.registryKey, SharedSuggestionProvider$ElementSuggestionType.ELEMENTS);
   }

   public Collection<String> getExamples() {
      return EXAMPLES;
   }
}
