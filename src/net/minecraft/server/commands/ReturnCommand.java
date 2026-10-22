package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.Commands;
import net.minecraft.commands.ExecutionCommandSource;

public class ReturnCommand {
   public static <T extends ExecutionCommandSource<T>> void register(final CommandDispatcher<T> dispatcher) {
      dispatcher.register(
         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)LiteralArgumentBuilder.literal("return")
                     .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
                  .then(RequiredArgumentBuilder.argument("value", IntegerArgumentType.integer()).executes(new ReturnCommand$ReturnValueCustomExecutor())))
               .then(LiteralArgumentBuilder.literal("fail").executes(new ReturnCommand$ReturnFailCustomExecutor())))
            .then(LiteralArgumentBuilder.literal("run").forward(dispatcher.getRoot(), new ReturnCommand$ReturnFromCommandCustomModifier(), false))
      );
   }
}
