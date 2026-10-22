package net.minecraft.server.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;

@FunctionalInterface
interface ExecuteCommand$CommandNumericPredicate {
   int test(CommandContext<CommandSourceStack> c) throws CommandSyntaxException;
}
