package net.minecraft.commands.arguments.item;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Dynamic;
import java.util.function.Predicate;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.component.predicates.DataComponentPredicate$Type;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

record ItemPredicateArgument$PredicateWrapper(Identifier id, Decoder<? extends Predicate<ItemStack>> type) {
   public ItemPredicateArgument$PredicateWrapper(final Holder$Reference<DataComponentPredicate$Type<?>> holder) {
      this(holder.key().identifier(), holder.value().codec().map(v -> v::matches));
   }

   public Predicate<ItemStack> decode(final ImmutableStringReader reader, final Dynamic<?> value) throws CommandSyntaxException {
      DataResult<? extends Predicate<ItemStack>> result = this.type.parse(value);
      return (Predicate<ItemStack>)result.getOrThrow(
         message -> ItemPredicateArgument.ERROR_MALFORMED_PREDICATE.createWithContext(reader, this.id.toString(), message)
      );
   }
}
