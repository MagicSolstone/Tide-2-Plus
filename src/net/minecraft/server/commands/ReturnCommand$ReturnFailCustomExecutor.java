package net.minecraft.server.commands;

import com.mojang.brigadier.context.ContextChain;
import net.minecraft.commands.ExecutionCommandSource;
import net.minecraft.commands.execution.ChainModifiers;
import net.minecraft.commands.execution.CustomCommandExecutor$CommandAdapter;
import net.minecraft.commands.execution.ExecutionControl;
import net.minecraft.commands.execution.Frame;

class ReturnCommand$ReturnFailCustomExecutor<T extends ExecutionCommandSource<T>> implements CustomCommandExecutor$CommandAdapter<T> {
   private ReturnCommand$ReturnFailCustomExecutor() {
   }

   public void run(final T sender, final ContextChain<T> currentStep, final ChainModifiers modifiers, final ExecutionControl<T> output) {
      sender.callback().onFailure();
      Frame frame = output.currentFrame();
      frame.returnFailure();
      frame.discard();
   }
}
