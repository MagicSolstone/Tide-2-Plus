package net.minecraft.commands.arguments.item;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.StringReader;
import java.util.stream.Stream;
import net.minecraft.resources.Identifier;
import net.minecraft.util.parsing.packrat.NamedRule;
import net.minecraft.util.parsing.packrat.commands.ResourceLookupRule;

class ComponentPredicateParser$ElementLookupRule<T, C, P> extends ResourceLookupRule<ComponentPredicateParser$Context<T, C, P>, T> {
   private ComponentPredicateParser$ElementLookupRule(
      final NamedRule<StringReader, Identifier> idParser, final ComponentPredicateParser$Context<T, C, P> context
   ) {
      super(idParser, context);
   }

   @Override
   protected T validateElement(final ImmutableStringReader reader, final Identifier id) throws Exception {
      return this.context.forElementType(reader, id);
   }

   @Override
   public Stream<Identifier> possibleResources() {
      return this.context.listElementTypes();
   }
}
