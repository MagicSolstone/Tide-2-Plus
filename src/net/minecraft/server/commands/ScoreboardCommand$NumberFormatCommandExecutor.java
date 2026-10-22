package net.minecraft.server.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.numbers.NumberFormat;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface ScoreboardCommand$NumberFormatCommandExecutor {
   int run(CommandContext<CommandSourceStack> context, @Nullable NumberFormat format) throws CommandSyntaxException;
}
