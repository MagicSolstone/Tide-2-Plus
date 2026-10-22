package net.minecraft.commands.execution.tasks;

import com.mojang.brigadier.context.ContextChain;
import java.util.List;
import net.minecraft.commands.ExecutionCommandSource;
import net.minecraft.commands.execution.ChainModifiers;
import net.minecraft.commands.execution.EntryAction;
import net.minecraft.commands.execution.ExecutionContext;
import net.minecraft.commands.execution.Frame;

public class BuildContexts$TopLevel<T extends ExecutionCommandSource<T>> extends BuildContexts<T> implements EntryAction<T> {
   private final T source;

   public BuildContexts$TopLevel(final String commandInput, final ContextChain<T> command, final T source) {
      super(commandInput, command);
      this.source = source;
   }

   @Override
   public void execute(final ExecutionContext<T> context, final Frame frame) {
      this.traceCommandStart(context, frame);
      this.execute(this.source, List.of(this.source), context, frame, ChainModifiers.DEFAULT);
   }
}
