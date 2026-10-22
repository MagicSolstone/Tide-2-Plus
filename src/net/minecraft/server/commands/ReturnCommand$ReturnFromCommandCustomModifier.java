package net.minecraft.server.commands;

import com.mojang.brigadier.context.ContextChain;
import java.util.List;
import net.minecraft.commands.ExecutionCommandSource;
import net.minecraft.commands.execution.ChainModifiers;
import net.minecraft.commands.execution.CustomModifierExecutor$ModifierAdapter;
import net.minecraft.commands.execution.ExecutionControl;
import net.minecraft.commands.execution.tasks.BuildContexts$Continuation;
import net.minecraft.commands.execution.tasks.FallthroughTask;

class ReturnCommand$ReturnFromCommandCustomModifier<T extends ExecutionCommandSource<T>> implements CustomModifierExecutor$ModifierAdapter<T> {
   private ReturnCommand$ReturnFromCommandCustomModifier() {
   }

   public void apply(
      final T originalSource, final List<T> currentSources, final ContextChain<T> currentStep, final ChainModifiers modifiers, final ExecutionControl<T> output
   ) {
      if (currentSources.isEmpty()) {
         if (modifiers.isReturn()) {
            output.queueNext(FallthroughTask.instance());
         }
      } else {
         output.currentFrame().discard();
         ContextChain<T> nextState = currentStep.nextStage();
         String command = nextState.getTopContext().getInput();
         output.queueNext(new BuildContexts$Continuation<>(command, nextState, modifiers.setReturn(), originalSource, currentSources));
      }
   }
}
