package net.minecraft.commands.execution.tasks;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ContextChain;
import com.mojang.brigadier.context.ContextChain.Stage;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.commands.CommandResultCallback;
import net.minecraft.commands.ExecutionCommandSource;
import net.minecraft.commands.execution.ChainModifiers;
import net.minecraft.commands.execution.CommandQueueEntry;
import net.minecraft.commands.execution.CustomCommandExecutor;
import net.minecraft.commands.execution.CustomModifierExecutor;
import net.minecraft.commands.execution.ExecutionContext;
import net.minecraft.commands.execution.ExecutionControl;
import net.minecraft.commands.execution.Frame;
import net.minecraft.commands.execution.TraceCallbacks;
import net.minecraft.network.chat.Component;

public class BuildContexts<T extends ExecutionCommandSource<T>> {
   @VisibleForTesting
   public static final DynamicCommandExceptionType ERROR_FORK_LIMIT_REACHED = new DynamicCommandExceptionType(
      limit -> Component.translatableEscape("command.forkLimit", limit)
   );
   private final String commandInput;
   private final ContextChain<T> command;

   public BuildContexts(final String commandInput, final ContextChain<T> command) {
      this.commandInput = commandInput;
      this.command = command;
   }

   protected void execute(
      final T originalSource, final List<T> initialSources, final ExecutionContext<T> context, final Frame frame, final ChainModifiers initialModifiers
   ) {
      ContextChain<T> currentStage = this.command;
      ChainModifiers modifiers = initialModifiers;
      List<T> currentSources = initialSources;
      if (currentStage.getStage() != Stage.EXECUTE) {
         context.profiler().push(() -> "prepare " + this.commandInput);

         try {
            for (int forkLimit = context.forkLimit(); currentStage.getStage() != Stage.EXECUTE; currentStage = currentStage.nextStage()) {
               CommandContext<T> contextToRun = currentStage.getTopContext();
               if (contextToRun.isForked()) {
                  modifiers = modifiers.setForked();
               }

               RedirectModifier<T> modifier = contextToRun.getRedirectModifier();
               if (modifier instanceof CustomModifierExecutor<T> customModifierExecutor) {
                  customModifierExecutor.apply(originalSource, currentSources, currentStage, modifiers, ExecutionControl.create(context, frame));
                  return;
               }

               if (modifier != null) {
                  context.incrementCost();
                  boolean forkedMode = modifiers.isForked();
                  List<T> nextSources = new ObjectArrayList();

                  for (T source : currentSources) {
                     try {
                        Collection<T> newSources = ContextChain.runModifier(contextToRun, source, (c, s, r) -> {}, forkedMode);
                        if (nextSources.size() + newSources.size() >= forkLimit) {
                           originalSource.handleError(ERROR_FORK_LIMIT_REACHED.create(forkLimit), forkedMode, context.tracer());
                           return;
                        }

                        nextSources.addAll(newSources);
                     } catch (CommandSyntaxException var20) {
                        source.handleError(var20, forkedMode, context.tracer());
                        if (!forkedMode) {
                           return;
                        }
                     }
                  }

                  currentSources = nextSources;
               }
            }
         } finally {
            context.profiler().pop();
         }
      }

      if (currentSources.isEmpty()) {
         if (modifiers.isReturn()) {
            context.queueNext(new CommandQueueEntry<>(frame, FallthroughTask.instance()));
         }
      } else {
         CommandContext<T> executeContext = currentStage.getTopContext();
         if (executeContext.getCommand() instanceof CustomCommandExecutor<T> customCommandExecutor) {
            ExecutionControl<T> executionControl = ExecutionControl.create(context, frame);

            for (T executionSource : currentSources) {
               customCommandExecutor.run(executionSource, currentStage, modifiers, executionControl);
            }
         } else {
            if (modifiers.isReturn()) {
               T returningSource = currentSources.get(0);
               returningSource = returningSource.withCallback(CommandResultCallback.chain(returningSource.callback(), frame.returnValueConsumer()));
               currentSources = List.of(returningSource);
            }

            ExecuteCommand<T> action = new ExecuteCommand<>(this.commandInput, modifiers, executeContext);
            ContinuationTask.schedule(context, frame, currentSources, (frame1, entrySource) -> new CommandQueueEntry<>(frame1, action.bind(entrySource)));
         }
      }
   }

   protected void traceCommandStart(final ExecutionContext<T> context, final Frame frame) {
      TraceCallbacks tracer = context.tracer();
      if (tracer != null) {
         tracer.onCommand(frame.depth(), this.commandInput);
      }
   }

   @Override
   public String toString() {
      return this.commandInput;
   }
}
