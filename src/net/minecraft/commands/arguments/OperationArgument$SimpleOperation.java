package net.minecraft.commands.arguments;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.scores.ScoreAccess;

@FunctionalInterface
interface OperationArgument$SimpleOperation extends OperationArgument$Operation {
   int apply(int a, int b) throws CommandSyntaxException;

   @Override
   default void apply(final ScoreAccess a, final ScoreAccess b) throws CommandSyntaxException {
      a.set(this.apply(a.get(), b.get()));
   }
}
