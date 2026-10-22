package net.minecraft.nbt;

import it.unimi.dsi.fastutil.chars.CharList;
import net.minecraft.util.parsing.packrat.commands.StringReaderTerms$TerminalCharacters;

class SnbtGrammar$5 extends StringReaderTerms$TerminalCharacters {
   SnbtGrammar$5(final CharList values) {
      super(values);
   }

   @Override
   protected boolean isAccepted(final char c) {
      return SnbtGrammar.canStartNumber(c);
   }
}
