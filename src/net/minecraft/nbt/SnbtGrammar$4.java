package net.minecraft.nbt;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.util.parsing.packrat.DelayedException;
import net.minecraft.util.parsing.packrat.commands.GreedyPredicateParseRule;

class SnbtGrammar$4 extends GreedyPredicateParseRule {
   SnbtGrammar$4(final int minSize, final DelayedException error) {
      super(minSize, error);
   }

   @Override
   protected boolean isAccepted(final char c) {
      return switch (c) {
         case '"', '\'', '\\' -> false;
         default -> true;
      };
   }
}
