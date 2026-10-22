package net.minecraft.commands.functions;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.List;
import net.minecraft.commands.ExecutionCommandSource;
import net.minecraft.commands.FunctionInstantiationException;
import net.minecraft.commands.execution.UnboundEntryAction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

class MacroFunction$MacroEntry<T extends ExecutionCommandSource<T>> implements MacroFunction$Entry<T> {
   private final StringTemplate template;
   private final IntList parameters;
   private final T compilationContext;

   public MacroFunction$MacroEntry(final StringTemplate template, final IntList parameters, final T compilationContext) {
      this.template = template;
      this.parameters = parameters;
      this.compilationContext = compilationContext;
   }

   @Override
   public IntList parameters() {
      return this.parameters;
   }

   @Override
   public UnboundEntryAction<T> instantiate(final List<String> substitutions, final CommandDispatcher<T> dispatcher, final Identifier functionId) throws FunctionInstantiationException {
      String command = this.template.substitute(substitutions);

      try {
         return CommandFunction.parseCommand(dispatcher, this.compilationContext, new StringReader(command));
      } catch (CommandSyntaxException var6) {
         throw new FunctionInstantiationException(
            Component.translatable("commands.function.error.parse", Component.translationArg(functionId), command, var6.getMessage())
         );
      }
   }
}
