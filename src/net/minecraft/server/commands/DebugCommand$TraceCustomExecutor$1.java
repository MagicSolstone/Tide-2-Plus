package net.minecraft.server.commands;

import java.io.PrintWriter;
import java.util.Objects;
import net.minecraft.commands.CommandResultCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.execution.ExecutionContext;
import net.minecraft.commands.execution.Frame;
import net.minecraft.commands.execution.tasks.CallFunction;
import net.minecraft.commands.functions.CommandFunction;
import net.minecraft.commands.functions.InstantiatedFunction;

class DebugCommand$TraceCustomExecutor$1 extends CallFunction<CommandSourceStack> {
   DebugCommand$TraceCustomExecutor$1(
      final DebugCommand$TraceCustomExecutor this$0,
      final InstantiatedFunction function,
      final CommandResultCallback resultCallback,
      final boolean returnParentFrame,
      final PrintWriter val$output,
      final CommandFunction val$function
   ) {
      this.val$output = val$outputx;
      this.val$function = val$functionx;
      Objects.requireNonNull(this$0);
      super(function, resultCallback, returnParentFrame);
   }

   public void execute(final CommandSourceStack sender, final ExecutionContext<CommandSourceStack> context, final Frame frame) {
      this.val$output.println(this.val$function.id());
      super.execute(sender, context, frame);
   }
}
