package net.minecraft.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Arrays;
import java.util.Collection;
import net.minecraft.advancements.predicates.MinMaxBounds$Doubles;
import net.minecraft.commands.CommandSourceStack;

public class RangeArgument$Floats implements RangeArgument<MinMaxBounds$Doubles> {
   private static final Collection<String> EXAMPLES = Arrays.asList("0..5.2", "0", "-5.4", "-100.76..", "..100");

   public static MinMaxBounds$Doubles getRange(final CommandContext<CommandSourceStack> context, final String name) {
      return (MinMaxBounds$Doubles)context.getArgument(name, MinMaxBounds$Doubles.class);
   }

   public MinMaxBounds$Doubles parse(final StringReader reader) throws CommandSyntaxException {
      return MinMaxBounds$Doubles.fromReader(reader);
   }

   public Collection<String> getExamples() {
      return EXAMPLES;
   }
}
