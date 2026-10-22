package net.minecraft.util.parsing.packrat.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.stream.Stream;
import net.minecraft.util.parsing.packrat.Control;
import net.minecraft.util.parsing.packrat.DelayedException;
import net.minecraft.util.parsing.packrat.ParseState;
import net.minecraft.util.parsing.packrat.Scope;
import net.minecraft.util.parsing.packrat.SuggestionSupplier;
import net.minecraft.util.parsing.packrat.Term;

public final class StringReaderTerms$TerminalWord implements Term<StringReader> {
   private final String value;
   private final DelayedException<CommandSyntaxException> error;
   private final SuggestionSupplier<StringReader> suggestions;

   public StringReaderTerms$TerminalWord(final String value) {
      this.value = value;
      this.error = DelayedException.create(CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect(), value);
      this.suggestions = s -> Stream.of(value);
   }

   @Override
   public boolean parse(final ParseState<StringReader> state, final Scope scope, final Control control) {
      state.input().skipWhitespace();
      int cursor = state.mark();
      String value = state.input().readUnquotedString();
      if (!value.equals(this.value)) {
         state.errorCollector().store(cursor, this.suggestions, this.error);
         return false;
      } else {
         return true;
      }
   }

   @Override
   public String toString() {
      return "terminal[" + this.value + "]";
   }
}
