package net.minecraft.nbt;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.util.parsing.packrat.DelayedException;
import net.minecraft.util.parsing.packrat.commands.NumberRunParseRule;

class SnbtGrammar$2 extends NumberRunParseRule {
   SnbtGrammar$2(final DelayedException noValueError, final DelayedException underscoreNotAllowedError) {
      super(noValueError, underscoreNotAllowedError);
   }

   @Override
   protected boolean isAccepted(final char c) {
      return switch (c) {
         case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '_' -> true;
         default -> false;
      };
   }
}
