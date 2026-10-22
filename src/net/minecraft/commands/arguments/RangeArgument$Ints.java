package net.minecraft.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Arrays;
import java.util.Collection;
import net.minecraft.advancements.predicates.MinMaxBounds$Ints;
import net.minecraft.commands.CommandSourceStack;

public class RangeArgument$Ints implements RangeArgument<MinMaxBounds$Ints> {
   private static final Collection<String> EXAMPLES = Arrays.asList("0..5", "0", "-5", "-100..", "..100");

   public static MinMaxBounds$Ints getRange(final CommandContext<CommandSourceStack> context, final String name) {
      return (MinMaxBounds$Ints)context.getArgument(name, MinMaxBounds$Ints.class);
   }

   public MinMaxBounds$Ints parse(final StringReader reader) throws CommandSyntaxException {
      return MinMaxBounds$Ints.fromReader(reader);
   }

   public Collection<String> getExamples() {
      return EXAMPLES;
   }
}
