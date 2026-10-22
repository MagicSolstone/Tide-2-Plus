package net.minecraft.util.parsing.packrat.commands;

import it.unimi.dsi.fastutil.chars.CharList;

class StringReaderTerms$2 extends StringReaderTerms$TerminalCharacters {
   StringReaderTerms$2(final CharList values, final char val$v1, final char val$v2) {
      super(values);
      this.val$v1 = val$v1x;
      this.val$v2 = val$v2x;
   }

   @Override
   protected boolean isAccepted(final char v) {
      return v == this.val$v1 || v == this.val$v2;
   }
}
