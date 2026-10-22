package net.minecraft.commands.arguments.item;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.core.HolderLookup$RegistryLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentPatch$Builder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.Item;
import org.apache.commons.lang3.mutable.MutableObject;

public class ItemParser {
   private static final DynamicCommandExceptionType ERROR_UNKNOWN_ITEM = new DynamicCommandExceptionType(
      id -> Component.translatableEscape("argument.item.id.invalid", id)
   );
   private static final DynamicCommandExceptionType ERROR_UNKNOWN_COMPONENT = new DynamicCommandExceptionType(
      id -> Component.translatableEscape("arguments.item.component.unknown", id)
   );
   private static final Dynamic2CommandExceptionType ERROR_MALFORMED_COMPONENT = new Dynamic2CommandExceptionType(
      (type, message) -> Component.translatableEscape("arguments.item.component.malformed", type, message)
   );
   private static final SimpleCommandExceptionType ERROR_EXPECTED_COMPONENT = new SimpleCommandExceptionType(
      Component.translatable("arguments.item.component.expected")
   );
   private static final DynamicCommandExceptionType ERROR_REPEATED_COMPONENT = new DynamicCommandExceptionType(
      id -> Component.translatableEscape("arguments.item.component.repeated", id)
   );
   public static final char SYNTAX_START_COMPONENTS = '[';
   public static final char SYNTAX_END_COMPONENTS = ']';
   public static final char SYNTAX_COMPONENT_SEPARATOR = ',';
   public static final char SYNTAX_COMPONENT_ASSIGNMENT = '=';
   public static final char SYNTAX_REMOVED_COMPONENT = '!';
   private static final Function<SuggestionsBuilder, CompletableFuture<Suggestions>> SUGGEST_NOTHING = SuggestionsBuilder::buildFuture;
   private final HolderLookup$RegistryLookup<Item> items;
   private final RegistryOps<Tag> registryOps;
   private final TagParser<Tag> tagParser;

   public ItemParser(final HolderLookup$Provider registries) {
      this.items = registries.lookupOrThrow(Registries.ITEM);
      this.registryOps = registries.createSerializationContext(NbtOps.INSTANCE);
      this.tagParser = TagParser.create(this.registryOps);
   }

   public ItemInput parse(final StringReader reader) throws CommandSyntaxException {
      MutableObject<Holder<Item>> itemResult = new MutableObject();
      DataComponentPatch$Builder componentsBuilder = DataComponentPatch.builder();
      this.parse(reader, new ItemParser$1(this, itemResult, componentsBuilder));
      Holder<Item> item = Objects.requireNonNull((Holder<Item>)itemResult.get(), "Parser gave no item");
      DataComponentPatch components = componentsBuilder.build();
      return new ItemInput(item, components);
   }

   public void parse(final StringReader reader, final ItemParser$Visitor visitor) throws CommandSyntaxException {
      int cursor = reader.getCursor();

      try {
         new ItemParser$State(this, reader, visitor).parse();
      } catch (CommandSyntaxException var5) {
         reader.setCursor(cursor);
         throw var5;
      }
   }

   public CompletableFuture<Suggestions> fillSuggestions(final SuggestionsBuilder builder) {
      StringReader reader = new StringReader(builder.getInput());
      reader.setCursor(builder.getStart());
      ItemParser$SuggestionsVisitor handler = new ItemParser$SuggestionsVisitor();
      ItemParser$State state = new ItemParser$State(this, reader, handler);

      try {
         state.parse();
      } catch (CommandSyntaxException var6) {
      }

      return handler.resolveSuggestions(builder, reader);
   }
}
