package net.minecraft.commands.arguments.item;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Dynamic;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.resources.Identifier;

public interface ComponentPredicateParser$Context<T, C, P> {
   T forElementType(ImmutableStringReader reader, Identifier id) throws CommandSyntaxException;

   Stream<Identifier> listElementTypes();

   T forTagType(ImmutableStringReader reader, Identifier id) throws CommandSyntaxException;

   Stream<Identifier> listTagTypes();

   C lookupComponentType(ImmutableStringReader reader, Identifier id) throws CommandSyntaxException;

   Stream<Identifier> listComponentTypes();

   T createComponentTest(ImmutableStringReader reader, C componentType, Dynamic<?> value) throws CommandSyntaxException;

   T createComponentTest(ImmutableStringReader reader, C componentType);

   P lookupPredicateType(ImmutableStringReader reader, Identifier id) throws CommandSyntaxException;

   Stream<Identifier> listPredicateTypes();

   T createPredicateTest(ImmutableStringReader reader, P predicateType, Dynamic<?> value) throws CommandSyntaxException;

   T negate(T value);

   T anyOf(List<T> alternatives);
}
