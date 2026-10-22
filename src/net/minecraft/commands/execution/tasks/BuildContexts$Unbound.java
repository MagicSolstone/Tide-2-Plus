package net.minecraft.commands.execution.tasks;

import com.mojang.brigadier.context.ContextChain;
import java.util.List;
import net.minecraft.commands.ExecutionCommandSource;
import net.minecraft.commands.execution.ChainModifiers;
import net.minecraft.commands.execution.ExecutionContext;
import net.minecraft.commands.execution.Frame;
import net.minecraft.commands.execution.UnboundEntryAction;

public class BuildContexts$Unbound<T extends ExecutionCommandSource<T>> extends BuildContexts<T> implements UnboundEntryAction<T> {
   public BuildContexts$Unbound(final String commandInput, final ContextChain<T> command) {
      super(commandInput, command);
   }

   public void execute(final T sender, final ExecutionContext<T> context, final Frame frame) {
      this.traceCommandStart(context, frame);
      this.execute(sender, List.of(sender), context, frame, ChainModifiers.DEFAULT);
   }
}
