package net.minecraft.server.commands;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import java.util.Collection;
import net.minecraft.commands.CommandResultCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.ExecutionCommandSource;
import net.minecraft.commands.FunctionInstantiationException;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.commands.arguments.NbtPathArgument$NbtPath;
import net.minecraft.commands.arguments.item.FunctionArgument;
import net.minecraft.commands.execution.ChainModifiers;
import net.minecraft.commands.execution.ExecutionControl;
import net.minecraft.commands.execution.tasks.CallFunction;
import net.minecraft.commands.execution.tasks.FallthroughTask;
import net.minecraft.commands.functions.CommandFunction;
import net.minecraft.commands.functions.InstantiatedFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.server.commands.data.DataAccessor;
import net.minecraft.server.commands.data.DataCommands;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import org.jspecify.annotations.Nullable;

public class FunctionCommand {
   private static final DynamicCommandExceptionType ERROR_ARGUMENT_NOT_COMPOUND = new DynamicCommandExceptionType(
      type -> Component.translatableEscape("commands.function.error.argument_not_compound", type)
   );
   private static final DynamicCommandExceptionType ERROR_NO_FUNCTIONS = new DynamicCommandExceptionType(
      name -> Component.translatableEscape("commands.function.scheduled.no_functions", name)
   );
   @VisibleForTesting
   public static final Dynamic2CommandExceptionType ERROR_FUNCTION_INSTANTATION_FAILURE = new Dynamic2CommandExceptionType(
      (id, reason) -> Component.translatableEscape("commands.function.instantiationFailure", id, reason)
   );
   public static final SuggestionProvider<CommandSourceStack> SUGGEST_FUNCTION = (c, p) -> {
      ServerFunctionManager manager = ((CommandSourceStack)c.getSource()).getServer().getFunctions();
      SharedSuggestionProvider.suggestResource(manager.getTagNames(), p, "#");
      return SharedSuggestionProvider.suggestResource(manager.getFunctionNames(), p);
   };
   private static final FunctionCommand$Callbacks<CommandSourceStack> FULL_CONTEXT_CALLBACKS = new FunctionCommand$5();

   public static void register(final CommandDispatcher<CommandSourceStack> dispatcher) {
      LiteralArgumentBuilder<CommandSourceStack> sources = Commands.literal("with");

      for (ArgProvider<DataAccessor> provider : DataCommands.SOURCE_PROVIDERS) {
         provider.wrap(
            sources,
            p -> p.executes(new FunctionCommand$1(provider))
               .then(Commands.argument("path", NbtPathArgument.nbtPath()).executes(new FunctionCommand$2(provider)))
         );
      }

      dispatcher.register(
         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("function").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
            .then(
               ((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("name", FunctionArgument.functions())
                        .suggests(SUGGEST_FUNCTION)
                        .executes(new FunctionCommand$3()))
                     .then(Commands.argument("arguments", CompoundTagArgument.compoundTag()).executes(new FunctionCommand$4())))
                  .then(sources)
            )
      );
   }

   private static CompoundTag getArgumentTag(final NbtPathArgument$NbtPath path, final DataAccessor accessor) throws CommandSyntaxException {
      Tag tag = DataCommands.getSingleTag(path, accessor);
      if (tag instanceof CompoundTag compoundTag) {
         return compoundTag;
      } else {
         throw ERROR_ARGUMENT_NOT_COMPOUND.create(tag.getType().getName());
      }
   }

   public static CommandSourceStack modifySenderForExecution(final CommandSourceStack sender) {
      return sender.withSuppressedOutput().withMaximumPermission(LevelBasedPermissionSet.GAMEMASTER);
   }

   public static <T extends ExecutionCommandSource<T>> void queueFunctions(
      final Collection<CommandFunction<T>> functions,
      @Nullable final CompoundTag arguments,
      final T originalSource,
      final T functionSource,
      final ExecutionControl<T> output,
      final FunctionCommand$Callbacks<T> callbacks,
      final ChainModifiers modifiers
   ) throws CommandSyntaxException {
      if (modifiers.isReturn()) {
         queueFunctionsAsReturn(functions, arguments, originalSource, functionSource, output, callbacks);
      } else {
         queueFunctionsNoReturn(functions, arguments, originalSource, functionSource, output, callbacks);
      }
   }

   private static <T extends ExecutionCommandSource<T>> void instantiateAndQueueFunctions(
      @Nullable final CompoundTag arguments,
      final ExecutionControl<T> output,
      final CommandDispatcher<T> dispatcher,
      final T noCallbackSource,
      final CommandFunction<T> function,
      final Identifier id,
      final CommandResultCallback functionResultCollector,
      final boolean returnParentFrame
   ) throws CommandSyntaxException {
      try {
         InstantiatedFunction<T> instantiatedFunction = function.instantiate(arguments, dispatcher);
         output.queueNext(new CallFunction<>(instantiatedFunction, functionResultCollector, returnParentFrame).bind(noCallbackSource));
      } catch (FunctionInstantiationException var9) {
         throw ERROR_FUNCTION_INSTANTATION_FAILURE.create(id, var9.messageComponent());
      }
   }

   private static <T extends ExecutionCommandSource<T>> CommandResultCallback decorateOutputIfNeeded(
      final T originalSource, final FunctionCommand$Callbacks<T> callbacks, final Identifier id, final CommandResultCallback callback
   ) {
      return originalSource.isSilent() ? callback : (success, result) -> {
         callbacks.signalResult(originalSource, id, result);
         callback.onResult(success, result);
      };
   }

   private static <T extends ExecutionCommandSource<T>> void queueFunctionsAsReturn(
      final Collection<CommandFunction<T>> functions,
      @Nullable final CompoundTag arguments,
      final T originalSource,
      final T functionSource,
      final ExecutionControl<T> output,
      final FunctionCommand$Callbacks<T> callbacks
   ) throws CommandSyntaxException {
      CommandDispatcher<T> dispatcher = originalSource.dispatcher();
      T noCallbackSource = functionSource.clearCallbacks();
      CommandResultCallback functionCommandOutputCallback = CommandResultCallback.chain(originalSource.callback(), output.currentFrame().returnValueConsumer());

      for (CommandFunction<T> function : functions) {
         Identifier id = function.id();
         CommandResultCallback functionResultCollector = decorateOutputIfNeeded(originalSource, callbacks, id, functionCommandOutputCallback);
         instantiateAndQueueFunctions(arguments, output, dispatcher, noCallbackSource, function, id, functionResultCollector, true);
      }

      output.queueNext(FallthroughTask.instance());
   }

   private static <T extends ExecutionCommandSource<T>> void queueFunctionsNoReturn(
      final Collection<CommandFunction<T>> functions,
      @Nullable final CompoundTag arguments,
      final T originalSource,
      final T functionSource,
      final ExecutionControl<T> output,
      final FunctionCommand$Callbacks<T> callbacks
   ) throws CommandSyntaxException {
      CommandDispatcher<T> dispatcher = originalSource.dispatcher();
      T noCallbackSource = functionSource.clearCallbacks();
      CommandResultCallback originalCallback = originalSource.callback();
      if (!functions.isEmpty()) {
         if (functions.size() == 1) {
            CommandFunction<T> function = functions.iterator().next();
            Identifier id = function.id();
            CommandResultCallback functionResultCollector = decorateOutputIfNeeded(originalSource, callbacks, id, originalCallback);
            instantiateAndQueueFunctions(arguments, output, dispatcher, noCallbackSource, function, id, functionResultCollector, false);
         } else if (originalCallback == CommandResultCallback.EMPTY) {
            for (CommandFunction<T> function : functions) {
               Identifier id = function.id();
               CommandResultCallback functionResultCollector = decorateOutputIfNeeded(originalSource, callbacks, id, originalCallback);
               instantiateAndQueueFunctions(arguments, output, dispatcher, noCallbackSource, function, id, functionResultCollector, false);
            }
         } else {
            FunctionCommand$1Accumulator accumulator = new FunctionCommand$1Accumulator();
            CommandResultCallback partialResultCallback = (success, result) -> accumulator.add(result);

            for (CommandFunction<T> function : functions) {
               Identifier id = function.id();
               CommandResultCallback functionResultCollector = decorateOutputIfNeeded(originalSource, callbacks, id, partialResultCallback);
               instantiateAndQueueFunctions(arguments, output, dispatcher, noCallbackSource, function, id, functionResultCollector, false);
            }

            output.queueNext((context, frame) -> {
               if (accumulator.anyResult) {
                  originalCallback.onSuccess(accumulator.sum);
               }
            });
         }
      }
   }
}
