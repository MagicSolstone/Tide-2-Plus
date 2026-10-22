package net.minecraft.nbt;

import net.minecraft.util.parsing.packrat.DelayedException;
import net.minecraft.util.parsing.packrat.commands.GreedyPredicateParseRule;

class SnbtGrammar$SimpleHexLiteralParseRule extends GreedyPredicateParseRule {
   public SnbtGrammar$SimpleHexLiteralParseRule(final int size) {
      super(size, size, DelayedException.create(SnbtGrammar.ERROR_EXPECTED_HEX_ESCAPE, String.valueOf(size)));
   }

   @Override
   protected boolean isAccepted(final char c) {
      return switch (c) {
         case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'a', 'b', 'c', 'd', 'e', 'f' -> true;
         default -> false;
      };
   }
}
