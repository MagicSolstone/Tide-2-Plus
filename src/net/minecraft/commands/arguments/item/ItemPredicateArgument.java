package net.minecraft.commands.arguments.item;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.advancements.predicates.MinMaxBounds$Ints;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;
import net.minecraft.util.parsing.packrat.commands.ParserBasedArgument;
import net.minecraft.world.item.ItemStack;

public class ItemPredicateArgument extends ParserBasedArgument<ItemPredicateArgument$Result> {
   private static final Collection<String> EXAMPLES = Arrays.asList("stick", "minecraft:stick", "#stick", "#stick{foo:'bar'}");
   private static final DynamicCommandExceptionType ERROR_UNKNOWN_ITEM = new DynamicCommandExceptionType(
      id -> Component.translatableEscape("argument.item.id.invalid", id)
   );
   private static final DynamicCommandExceptionType ERROR_UNKNOWN_TAG = new DynamicCommandExceptionType(
      tag -> Component.translatableEscape("arguments.item.tag.unknown", tag)
   );
   private static final DynamicCommandExceptionType ERROR_UNKNOWN_COMPONENT = new DynamicCommandExceptionType(
      id -> Component.translatableEscape("arguments.item.component.unknown", id)
   );
   private static final Dynamic2CommandExceptionType ERROR_MALFORMED_COMPONENT = new Dynamic2CommandExceptionType(
      (type, message) -> Component.translatableEscape("arguments.item.component.malformed", type, message)
   );
   private static final DynamicCommandExceptionType ERROR_UNKNOWN_PREDICATE = new DynamicCommandExceptionType(
      id -> Component.translatableEscape("arguments.item.predicate.unknown", id)
   );
   private static final Dynamic2CommandExceptionType ERROR_MALFORMED_PREDICATE = new Dynamic2CommandExceptionType(
      (type, message) -> Component.translatableEscape("arguments.item.predicate.malformed", type, message)
   );
   private static final Identifier COUNT_ID = Identifier.withDefaultNamespace("count");
   private static final Map<Identifier, ItemPredicateArgument$ComponentWrapper> PSEUDO_COMPONENTS = Stream.of(
         new ItemPredicateArgument$ComponentWrapper(
            COUNT_ID, itemStack -> true, MinMaxBounds$Ints.CODEC.map(range -> itemStack -> range.matches(itemStack.getCount()))
         )
      )
      .collect(Collectors.toUnmodifiableMap(ItemPredicateArgument$ComponentWrapper::id, e -> (ItemPredicateArgument$ComponentWrapper)e));
   private static final Map<Identifier, ItemPredicateArgument$PredicateWrapper> PSEUDO_PREDICATES = Stream.of(
         new ItemPredicateArgument$PredicateWrapper(COUNT_ID, MinMaxBounds$Ints.CODEC.map(range -> itemStack -> range.matches(itemStack.getCount())))
      )
      .collect(Collectors.toUnmodifiableMap(ItemPredicateArgument$PredicateWrapper::id, e -> (ItemPredicateArgument$PredicateWrapper)e));

   private static ItemPredicateArgument$PredicateWrapper createComponentExistencePredicate(final Holder$Reference<DataComponentType<?>> componentId) {
      Predicate<ItemStack> componentExists = itemStack -> itemStack.has(componentId.value());
      return new ItemPredicateArgument$PredicateWrapper(componentId.key().identifier(), Unit.CODEC.map(unit -> componentExists));
   }

   public ItemPredicateArgument(final CommandBuildContext registries) {
      super(ComponentPredicateParser.createGrammar(new ItemPredicateArgument$Context(registries)).mapResult(predicates -> Util.allOf(predicates)::test));
   }

   public static ItemPredicateArgument itemPredicate(final CommandBuildContext context) {
      return new ItemPredicateArgument(context);
   }

   public static ItemPredicateArgument$Result getItemPredicate(final CommandContext<CommandSourceStack> context, final String name) {
      return (ItemPredicateArgument$Result)context.getArgument(name, ItemPredicateArgument$Result.class);
   }

   public Collection<String> getExamples() {
      return EXAMPLES;
   }
}
