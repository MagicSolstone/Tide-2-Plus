package net.minecraft.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.serialization.DataResult;
import java.util.function.Function;
import net.minecraft.network.chat.Component;

public abstract class CompilableString$CommandParserHelper<T> implements Function<String, DataResult<T>> {
   private static final DynamicCommandExceptionType TRAILING_DATA = new DynamicCommandExceptionType(
      commandAndRemainder -> Component.translatableEscape("command.trailing_data", commandAndRemainder)
   );

   public final DataResult<T> apply(final String contents) {
      StringReader reader = new StringReader(contents);

      try {
         T result = this.parse(reader);
         if (reader.canRead()) {
            String parsed = reader.getString().substring(0, reader.getCursor());
            String leftovers = reader.getString().substring(reader.getCursor());
            throw TRAILING_DATA.create(parsed + "[" + leftovers + "]");
         } else {
            return DataResult.success(result);
         }
      } catch (CommandSyntaxException var6) {
         return DataResult.error(() -> this.errorMessage(contents, var6));
      }
   }

   protected abstract T parse(StringReader reader) throws CommandSyntaxException;

   protected abstract String errorMessage(String original, CommandSyntaxException exception);
}
