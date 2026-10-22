package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ContextChain;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import net.minecraft.commands.CommandResultCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.FunctionInstantiationException;
import net.minecraft.commands.arguments.item.FunctionArgument;
import net.minecraft.commands.execution.ChainModifiers;
import net.minecraft.commands.execution.CustomCommandExecutor$CommandAdapter;
import net.minecraft.commands.execution.CustomCommandExecutor$WithErrorHandling;
import net.minecraft.commands.execution.ExecutionControl;
import net.minecraft.commands.functions.CommandFunction;
import net.minecraft.commands.functions.InstantiatedFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.util.Util;

class DebugCommand$TraceCustomExecutor
   extends CustomCommandExecutor$WithErrorHandling<CommandSourceStack>
   implements CustomCommandExecutor$CommandAdapter<CommandSourceStack> {
   private static final CommandResponseTracker$DispatchWithArg<Component, CommandFunction<CommandSourceStack>, String> RESPONSE_TRACE = new CommandResponseTracker$DispatchWithArg<>(
      (function, totalValue, outputName) -> Component.translatable(
         "commands.debug.function.success.single", totalValue, Component.translationArg(function.id()), outputName
      ),
      (functionCount, totalValue, outputName) -> Component.translatable("commands.debug.function.success.multiple", totalValue, functionCount, outputName)
   );

   private DebugCommand$TraceCustomExecutor() {
   }

   public void runGuarded(
      final CommandSourceStack source,
      final ContextChain<CommandSourceStack> currentStep,
      final ChainModifiers modifiers,
      final ExecutionControl<CommandSourceStack> context
   ) throws CommandSyntaxException {
      if (modifiers.isReturn()) {
         throw DebugCommand.NO_RETURN_RUN.create();
      } else if (context.tracer() != null) {
         throw DebugCommand.NO_RECURSIVE_TRACES.create();
      } else {
         CommandContext<CommandSourceStack> currentContext = currentStep.getTopContext();
         Collection<CommandFunction<CommandSourceStack>> functions = FunctionArgument.getFunctions(currentContext, "name");
         MinecraftServer server = source.getServer();
         String outputName = "debug-trace-" + Util.getFilenameFormattedDateTime() + ".txt";
         CommandDispatcher<CommandSourceStack> dispatcher = source.getServer().getFunctions().getDispatcher();
         CommandResponseTracker<CommandFunction<CommandSourceStack>> tracker = CommandResponseTracker.create();

         try {
            Path dirPath = server.getFile("debug");
            Files.createDirectories(dirPath);
            PrintWriter output = new PrintWriter(Files.newBufferedWriter(dirPath.resolve(outputName), StandardCharsets.UTF_8));
            DebugCommand$Tracer tracer = new DebugCommand$Tracer(output);
            context.tracer(tracer);

            for (CommandFunction<CommandSourceStack> function : functions) {
               try {
                  CommandSourceStack functionSource = source.withSource(tracer).withMaximumPermission(LevelBasedPermissionSet.GAMEMASTER);
                  InstantiatedFunction<CommandSourceStack> instantiatedFunction = function.instantiate(null, dispatcher);
                  context.queueNext(
                     new DebugCommand$TraceCustomExecutor$1(this, instantiatedFunction, CommandResultCallback.EMPTY, false, output, function)
                        .bind(functionSource)
                  );
                  tracker.track(function, instantiatedFunction.entries().size());
               } catch (FunctionInstantiationException var18) {
                  source.sendFailure(var18.messageComponent());
               }
            }
         } catch (IOException | UncheckedIOException var19) {
            DebugCommand.LOGGER.warn("Tracing failed", var19);
            source.sendFailure(Component.translatable("commands.debug.function.traceFailed"));
         }

         context.queueNext((var3, var4) -> source.sendSuccess(() -> tracker.dispatch(CommandResponseTracker$ElementType.ANY, RESPONSE_TRACE, outputName), true));
      }
   }
}
