package net.minecraft.nbt;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.util.parsing.packrat.DelayedException;
import net.minecraft.util.parsing.packrat.commands.NumberRunParseRule;

class SnbtGrammar$1 extends NumberRunParseRule {
   SnbtGrammar$1(final DelayedException noValueError, final DelayedException underscoreNotAllowedError) {
      super(noValueError, underscoreNotAllowedError);
   }

   @Override
   protected boolean isAccepted(final char c) {
      return switch (c) {
         case '0', '1', '_' -> true;
         default -> false;
      };
   }
}
