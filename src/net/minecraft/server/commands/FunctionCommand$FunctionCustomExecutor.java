package net.minecraft.server.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ContextChain;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.item.FunctionArgument;
import net.minecraft.commands.execution.ChainModifiers;
import net.minecraft.commands.execution.CustomCommandExecutor$CommandAdapter;
import net.minecraft.commands.execution.CustomCommandExecutor$WithErrorHandling;
import net.minecraft.commands.execution.ExecutionControl;
import net.minecraft.commands.functions.CommandFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

abstract class FunctionCommand$FunctionCustomExecutor
   extends CustomCommandExecutor$WithErrorHandling<CommandSourceStack>
   implements CustomCommandExecutor$CommandAdapter<CommandSourceStack> {
   private FunctionCommand$FunctionCustomExecutor() {
   }

   @Nullable
   protected abstract CompoundTag arguments(final CommandContext<CommandSourceStack> context) throws CommandSyntaxException;

   public void runGuarded(
      final CommandSourceStack sender,
      final ContextChain<CommandSourceStack> currentStep,
      final ChainModifiers modifiers,
      final ExecutionControl<CommandSourceStack> output
   ) throws CommandSyntaxException {
      CommandContext<CommandSourceStack> currentContext = currentStep.getTopContext().copyFor(sender);
      Pair<Identifier, Collection<CommandFunction<CommandSourceStack>>> nameAndFunctions = FunctionArgument.getFunctionCollection(currentContext, "name");
      Collection<CommandFunction<CommandSourceStack>> functions = (Collection<CommandFunction<CommandSourceStack>>)nameAndFunctions.getSecond();
      if (functions.isEmpty()) {
         throw FunctionCommand.ERROR_NO_FUNCTIONS.create(Component.translationArg((Identifier)nameAndFunctions.getFirst()));
      } else {
         CompoundTag arguments = this.arguments(currentContext);
         CommandSourceStack commonFunctionContext = FunctionCommand.modifySenderForExecution(sender);
         if (functions.size() == 1) {
            sender.sendSuccess(
               () -> Component.translatable("commands.function.scheduled.single", Component.translationArg(functions.iterator().next().id())), true
            );
         } else {
            sender.sendSuccess(
               () -> Component.translatable(
                  "commands.function.scheduled.multiple",
                  ComponentUtils.formatList(functions.stream().map(CommandFunction::id).toList(), Component::translationArg)
               ),
               true
            );
         }

         FunctionCommand.queueFunctions(functions, arguments, sender, commonFunctionContext, output, FunctionCommand.FULL_CONTEXT_CALLBACKS, modifiers);
      }
   }
}
