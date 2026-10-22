package net.minecraft.commands.execution;

import com.mojang.brigadier.context.ContextChain;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.ExecutionCommandSource;
import org.jspecify.annotations.Nullable;

public abstract class CustomCommandExecutor$WithErrorHandling<T extends ExecutionCommandSource<T>> implements CustomCommandExecutor<T> {
   public final void run(final T sender, final ContextChain<T> currentStep, final ChainModifiers modifiers, final ExecutionControl<T> output) {
      try {
         this.runGuarded(sender, currentStep, modifiers, output);
      } catch (CommandSyntaxException var6) {
         this.onError(var6, sender, modifiers, output.tracer());
         sender.callback().onFailure();
      }
   }

   protected void onError(final CommandSyntaxException e, final T sender, final ChainModifiers modifiers, @Nullable final TraceCallbacks tracer) {
      sender.handleError(e, modifiers.isForked(), tracer);
   }

   protected abstract void runGuarded(T sender, ContextChain<T> currentStep, ChainModifiers modifiers, ExecutionControl<T> output) throws CommandSyntaxException;
}
