package net.minecraft.commands.execution.tasks;

import com.mojang.brigadier.context.ContextChain;
import java.util.List;
import net.minecraft.commands.ExecutionCommandSource;
import net.minecraft.commands.execution.ChainModifiers;
import net.minecraft.commands.execution.EntryAction;
import net.minecraft.commands.execution.ExecutionContext;
import net.minecraft.commands.execution.Frame;

public class BuildContexts$Continuation<T extends ExecutionCommandSource<T>> extends BuildContexts<T> implements EntryAction<T> {
   private final ChainModifiers modifiers;
   private final T originalSource;
   private final List<T> sources;

   public BuildContexts$Continuation(
      final String commandInput, final ContextChain<T> command, final ChainModifiers modifiers, final T originalSource, final List<T> sources
   ) {
      super(commandInput, command);
      this.originalSource = originalSource;
      this.sources = sources;
      this.modifiers = modifiers;
   }

   @Override
   public void execute(final ExecutionContext<T> context, final Frame frame) {
      this.execute(this.originalSource, this.sources, context, frame, this.modifiers);
   }
}
