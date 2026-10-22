package net.minecraft.commands.functions;

import com.mojang.brigadier.CommandDispatcher;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntLists;
import java.util.List;
import net.minecraft.commands.execution.UnboundEntryAction;
import net.minecraft.resources.Identifier;

class MacroFunction$PlainTextEntry<T> implements MacroFunction$Entry<T> {
   private final UnboundEntryAction<T> compiledAction;

   public MacroFunction$PlainTextEntry(final UnboundEntryAction<T> compiledAction) {
      this.compiledAction = compiledAction;
   }

   @Override
   public IntList parameters() {
      return IntLists.emptyList();
   }

   @Override
   public UnboundEntryAction<T> instantiate(final List<String> substitutions, final CommandDispatcher<T> dispatcher, final Identifier functionId) {
      return this.compiledAction;
   }
}
