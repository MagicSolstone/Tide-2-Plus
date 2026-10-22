package net.minecraft.util.parsing.packrat.commands;

import it.unimi.dsi.fastutil.chars.CharList;

class StringReaderTerms$1 extends StringReaderTerms$TerminalCharacters {
   StringReaderTerms$1(final CharList values, final char val$value) {
      super(values);
      this.val$value = val$valuex;
   }

   @Override
   protected boolean isAccepted(final char v) {
      return this.val$value == v;
   }
}
