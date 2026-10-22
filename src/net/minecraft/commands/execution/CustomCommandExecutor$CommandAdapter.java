package net.minecraft.commands.execution;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public interface CustomCommandExecutor$CommandAdapter<T> extends CustomCommandExecutor<T>, Command<T> {
   default int run(final CommandContext<T> context) throws CommandSyntaxException {
      throw new UnsupportedOperationException("This function should not run");
   }
}
