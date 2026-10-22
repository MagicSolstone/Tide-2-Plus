package net.minecraft.util.parsing.packrat.commands;

import com.mojang.brigadier.StringReader;
import it.unimi.dsi.fastutil.chars.CharList;
import net.minecraft.util.parsing.packrat.Term;

public interface StringReaderTerms {
   static Term<StringReader> word(final String value) {
      return new StringReaderTerms$TerminalWord(value);
   }

   static Term<StringReader> character(final char value) {
      return new StringReaderTerms$1(CharList.of(value), value);
   }

   static Term<StringReader> characters(final char v1, final char v2) {
      return new StringReaderTerms$2(CharList.of(v1, v2), v1, v2);
   }

   static StringReader createReader(final String contents, final int cursor) {
      StringReader reader = new StringReader(contents);
      reader.setCursor(cursor);
      return reader;
   }
}
