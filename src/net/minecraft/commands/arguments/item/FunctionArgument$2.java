package net.minecraft.commands.arguments.item;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.functions.CommandFunction;
import net.minecraft.resources.Identifier;

class FunctionArgument$2 implements FunctionArgument$Result {
   FunctionArgument$2(final FunctionArgument this$0, final Identifier val$id) {
      this.val$id = val$idx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public Collection<CommandFunction<CommandSourceStack>> create(final CommandContext<CommandSourceStack> c) throws CommandSyntaxException {
      return Collections.singleton(FunctionArgument.getFunction(c, this.val$id));
   }

   @Override
   public Pair<Identifier, Either<CommandFunction<CommandSourceStack>, Collection<CommandFunction<CommandSourceStack>>>> unwrap(
      final CommandContext<CommandSourceStack> context
   ) throws CommandSyntaxException {
      return Pair.of(this.val$id, Either.left(FunctionArgument.getFunction(context, this.val$id)));
   }

   @Override
   public Pair<Identifier, Collection<CommandFunction<CommandSourceStack>>> unwrapToCollection(final CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      return Pair.of(this.val$id, Collections.singleton(FunctionArgument.getFunction(context, this.val$id)));
   }
}
