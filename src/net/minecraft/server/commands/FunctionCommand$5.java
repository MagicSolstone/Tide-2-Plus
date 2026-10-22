package net.minecraft.server.commands;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

class FunctionCommand$5 implements FunctionCommand$Callbacks<CommandSourceStack> {
   public void signalResult(final CommandSourceStack originalSource, final Identifier id, final int newValue) {
      originalSource.sendSuccess(() -> Component.translatable("commands.function.result", Component.translationArg(id), newValue), true);
   }
}
