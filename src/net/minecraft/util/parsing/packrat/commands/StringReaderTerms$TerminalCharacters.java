package net.minecraft.util.parsing.packrat.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import it.unimi.dsi.fastutil.chars.CharList;
import java.util.stream.Collectors;
import net.minecraft.util.parsing.packrat.Control;
import net.minecraft.util.parsing.packrat.DelayedException;
import net.minecraft.util.parsing.packrat.ParseState;
import net.minecraft.util.parsing.packrat.Scope;
import net.minecraft.util.parsing.packrat.SuggestionSupplier;
import net.minecraft.util.parsing.packrat.Term;

public abstract class StringReaderTerms$TerminalCharacters implements Term<StringReader> {
   private final DelayedException<CommandSyntaxException> error;
   private final SuggestionSupplier<StringReader> suggestions;

   public StringReaderTerms$TerminalCharacters(final CharList values) {
      String joinedValues = values.intStream().mapToObj(Character::toString).collect(Collectors.joining("|"));
      this.error = DelayedException.create(CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect(), joinedValues);
      this.suggestions = s -> values.intStream().mapToObj(Character::toString);
   }

   @Override
   public boolean parse(final ParseState<StringReader> state, final Scope scope, final Control control) {
      state.input().skipWhitespace();
      int cursor = state.mark();
      if (state.input().canRead() && this.isAccepted(state.input().read())) {
         return true;
      } else {
         state.errorCollector().store(cursor, this.suggestions, this.error);
         return false;
      }
   }

   protected abstract boolean isAccepted(char value);
}
