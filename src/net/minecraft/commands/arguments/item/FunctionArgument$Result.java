package net.minecraft.commands.arguments.item;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.functions.CommandFunction;
import net.minecraft.resources.Identifier;

public interface FunctionArgument$Result {
   Collection<CommandFunction<CommandSourceStack>> create(CommandContext<CommandSourceStack> context) throws CommandSyntaxException;

   Pair<Identifier, Either<CommandFunction<CommandSourceStack>, Collection<CommandFunction<CommandSourceStack>>>> unwrap(
      CommandContext<CommandSourceStack> context
   ) throws CommandSyntaxException;

   Pair<Identifier, Collection<CommandFunction<CommandSourceStack>>> unwrapToCollection(CommandContext<CommandSourceStack> context) throws CommandSyntaxException;
}
