package net.minecraft.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class OperationArgument implements ArgumentType<OperationArgument$Operation> {
   private static final Collection<String> EXAMPLES = Arrays.asList("=", ">", "<");
   private static final SimpleCommandExceptionType ERROR_INVALID_OPERATION = new SimpleCommandExceptionType(
      Component.translatable("arguments.operation.invalid")
   );
   private static final SimpleCommandExceptionType ERROR_DIVIDE_BY_ZERO = new SimpleCommandExceptionType(Component.translatable("arguments.operation.div0"));

   public static OperationArgument operation() {
      return new OperationArgument();
   }

   public static OperationArgument$Operation getOperation(final CommandContext<CommandSourceStack> context, final String name) {
      return (OperationArgument$Operation)context.getArgument(name, OperationArgument$Operation.class);
   }

   public OperationArgument$Operation parse(final StringReader reader) throws CommandSyntaxException {
      if (!reader.canRead()) {
         throw ERROR_INVALID_OPERATION.createWithContext(reader);
      } else {
         int start = reader.getCursor();

         while (reader.canRead() && reader.peek() != ' ') {
            reader.skip();
         }

         return getOperation(reader.getString().substring(start, reader.getCursor()));
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
      return SharedSuggestionProvider.suggest(new String[]{"=", "+=", "-=", "*=", "/=", "%=", "<", ">", "><"}, builder);
   }

   public Collection<String> getExamples() {
      return EXAMPLES;
   }

   private static OperationArgument$Operation getOperation(final String op) throws CommandSyntaxException {
      return (OperationArgument$Operation)(op.equals("><") ? (a, b) -> {
         int swap = a.get();
         a.set(b.get());
         b.set(swap);
      } : getSimpleOperation(op));
   }

   private static OperationArgument$SimpleOperation getSimpleOperation(final String op) throws CommandSyntaxException {
      return switch (op) {
         case "=" -> (a, b) -> b;
         case "+=" -> Integer::sum;
         case "-=" -> (a, b) -> a - b;
         case "*=" -> (a, b) -> a * b;
         case "/=" -> (a, b) -> {
            if (b == 0) {
               throw ERROR_DIVIDE_BY_ZERO.create();
            } else {
               return Mth.floorDiv(a, b);
            }
         };
         case "%=" -> (a, b) -> {
            if (b == 0) {
               throw ERROR_DIVIDE_BY_ZERO.create();
            } else {
               return Mth.positiveModulo(a, b);
            }
         };
         case "<" -> Math::min;
         case ">" -> Math::max;
         default -> throw ERROR_INVALID_OPERATION.create();
      };
   }
}
