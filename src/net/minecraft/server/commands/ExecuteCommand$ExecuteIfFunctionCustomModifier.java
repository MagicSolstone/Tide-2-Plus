package net.minecraft.server.commands;

import com.mojang.brigadier.context.ContextChain;
import java.util.List;
import java.util.function.IntPredicate;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.item.FunctionArgument;
import net.minecraft.commands.execution.ChainModifiers;
import net.minecraft.commands.execution.CustomModifierExecutor$ModifierAdapter;
import net.minecraft.commands.execution.ExecutionControl;

class ExecuteCommand$ExecuteIfFunctionCustomModifier implements CustomModifierExecutor$ModifierAdapter<CommandSourceStack> {
   private final IntPredicate check;

   private ExecuteCommand$ExecuteIfFunctionCustomModifier(final boolean check) {
      this.check = check ? value -> value != 0 : value -> value == 0;
   }

   public void apply(
      final CommandSourceStack originalSource,
      final List<CommandSourceStack> currentSources,
      final ContextChain<CommandSourceStack> currentStep,
      final ChainModifiers modifiers,
      final ExecutionControl<CommandSourceStack> output
   ) {
      ExecuteCommand.scheduleFunctionConditionsAndTest(
         originalSource,
         currentSources,
         FunctionCommand::modifySenderForExecution,
         this.check,
         currentStep,
         null,
         output,
         c -> FunctionArgument.getFunctions(c, "name"),
         modifiers
      );
   }
}
