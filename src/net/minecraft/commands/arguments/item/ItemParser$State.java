package net.minecraft.commands.arguments.item;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.serialization.DataResult;
import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;

class ItemParser$State {
   private final StringReader reader;
   private final ItemParser$Visitor visitor;

   private ItemParser$State(final ItemParser this$0, final StringReader reader, final ItemParser$Visitor visitor) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.reader = reader;
      this.visitor = visitor;
   }

   public void parse() throws CommandSyntaxException {
      this.visitor.visitSuggestions(this::suggestItem);
      this.readItem();
      this.visitor.visitSuggestions(this::suggestStartComponents);
      if (this.reader.canRead() && this.reader.peek() == '[') {
         this.visitor.visitSuggestions(ItemParser.SUGGEST_NOTHING);
         this.readComponents();
      }
   }

   private void readItem() throws CommandSyntaxException {
      int cursor = this.reader.getCursor();
      Identifier id = Identifier.read(this.reader);
      this.visitor.visitItem(this.this$0.items.get(ResourceKey.create(Registries.ITEM, id)).orElseThrow(() -> {
         this.reader.setCursor(cursor);
         return ItemParser.ERROR_UNKNOWN_ITEM.createWithContext(this.reader, id);
      }));
   }

   private void readComponents() throws CommandSyntaxException {
      this.reader.expect('[');
      this.visitor.visitSuggestions(this::suggestComponentAssignmentOrRemoval);
      Set<DataComponentType<?>> visitedComponents = new ReferenceArraySet();

      while (this.reader.canRead() && this.reader.peek() != ']') {
         this.reader.skipWhitespace();
         if (this.reader.canRead() && this.reader.peek() == '!') {
            this.reader.skip();
            this.visitor.visitSuggestions(this::suggestComponent);
            DataComponentType<?> componentType = readComponentType(this.reader);
            if (!visitedComponents.add(componentType)) {
               throw ItemParser.ERROR_REPEATED_COMPONENT.create(componentType);
            }

            this.visitor.visitRemovedComponent(componentType);
            this.visitor.visitSuggestions(ItemParser.SUGGEST_NOTHING);
            this.reader.skipWhitespace();
         } else {
            DataComponentType<?> componentType = readComponentType(this.reader);
            if (!visitedComponents.add(componentType)) {
               throw ItemParser.ERROR_REPEATED_COMPONENT.create(componentType);
            }

            this.visitor.visitSuggestions(this::suggestAssignment);
            this.reader.skipWhitespace();
            this.reader.expect('=');
            this.visitor.visitSuggestions(ItemParser.SUGGEST_NOTHING);
            this.reader.skipWhitespace();
            this.readComponent(this.this$0.tagParser, this.this$0.registryOps, componentType);
            this.reader.skipWhitespace();
         }

         this.visitor.visitSuggestions(this::suggestNextOrEndComponents);
         if (!this.reader.canRead() || this.reader.peek() != ',') {
            break;
         }

         this.reader.skip();
         this.reader.skipWhitespace();
         this.visitor.visitSuggestions(this::suggestComponentAssignmentOrRemoval);
         if (!this.reader.canRead()) {
            throw ItemParser.ERROR_EXPECTED_COMPONENT.createWithContext(this.reader);
         }
      }

      this.reader.expect(']');
      this.visitor.visitSuggestions(ItemParser.SUGGEST_NOTHING);
   }

   public static DataComponentType<?> readComponentType(final StringReader reader) throws CommandSyntaxException {
      if (!reader.canRead()) {
         throw ItemParser.ERROR_EXPECTED_COMPONENT.createWithContext(reader);
      } else {
         int cursor = reader.getCursor();
         Identifier id = Identifier.read(reader);
         DataComponentType<?> component = BuiltInRegistries.DATA_COMPONENT_TYPE.getValue(id);
         if (component != null && !component.isTransient()) {
            return component;
         } else {
            reader.setCursor(cursor);
            throw ItemParser.ERROR_UNKNOWN_COMPONENT.createWithContext(reader, id);
         }
      }
   }

   private <T, O> void readComponent(final TagParser<O> tagParser, final RegistryOps<O> registryOps, final DataComponentType<T> componentType) throws CommandSyntaxException {
      int cursor = this.reader.getCursor();
      O tag = tagParser.parseAsArgument(this.reader);
      DataResult<T> result = componentType.codecOrThrow().parse(registryOps, tag);
      this.visitor.visitComponent(componentType, (T)result.getOrThrow(message -> {
         this.reader.setCursor(cursor);
         return ItemParser.ERROR_MALFORMED_COMPONENT.createWithContext(this.reader, componentType.toString(), message);
      }));
   }

   private CompletableFuture<Suggestions> suggestStartComponents(final SuggestionsBuilder builder) {
      if (builder.getRemaining().isEmpty()) {
         builder.suggest(String.valueOf('['));
      }

      return builder.buildFuture();
   }

   private CompletableFuture<Suggestions> suggestNextOrEndComponents(final SuggestionsBuilder builder) {
      if (builder.getRemaining().isEmpty()) {
         builder.suggest(String.valueOf(','));
         builder.suggest(String.valueOf(']'));
      }

      return builder.buildFuture();
   }

   private CompletableFuture<Suggestions> suggestAssignment(final SuggestionsBuilder builder) {
      if (builder.getRemaining().isEmpty()) {
         builder.suggest(String.valueOf('='));
      }

      return builder.buildFuture();
   }

   private CompletableFuture<Suggestions> suggestItem(final SuggestionsBuilder builder) {
      return SharedSuggestionProvider.suggestResource(this.this$0.items.listElementIds().map(ResourceKey::identifier), builder);
   }

   private CompletableFuture<Suggestions> suggestComponentAssignmentOrRemoval(final SuggestionsBuilder builder) {
      builder.suggest(String.valueOf('!'));
      return this.suggestComponent(builder, String.valueOf('='));
   }

   private CompletableFuture<Suggestions> suggestComponent(final SuggestionsBuilder builder) {
      return this.suggestComponent(builder, "");
   }

   private CompletableFuture<Suggestions> suggestComponent(final SuggestionsBuilder builder, final String suffix) {
      String contents = builder.getRemaining().toLowerCase(Locale.ROOT);
      SharedSuggestionProvider.filterResources(BuiltInRegistries.DATA_COMPONENT_TYPE.entrySet(), contents, entry -> entry.getKey().identifier(), entry -> {
         DataComponentType<?> type = entry.getValue();
         if (type.codec() != null) {
            Identifier id = entry.getKey().identifier();
            builder.suggest(id + suffix);
         }
      });
      return builder.buildFuture();
   }
}
