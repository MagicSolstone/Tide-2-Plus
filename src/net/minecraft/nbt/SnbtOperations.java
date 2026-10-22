package net.minecraft.nbt;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Map;
import net.minecraft.network.chat.Component;
import net.minecraft.util.parsing.packrat.DelayedException;
import net.minecraft.util.parsing.packrat.SuggestionSupplier;

public class SnbtOperations {
   private static final DelayedException<CommandSyntaxException> ERROR_EXPECTED_STRING_UUID = DelayedException.create(
      new SimpleCommandExceptionType(Component.translatable("snbt.parser.expected_string_uuid"))
   );
   private static final DelayedException<CommandSyntaxException> ERROR_EXPECTED_NUMBER_OR_BOOLEAN = DelayedException.create(
      new SimpleCommandExceptionType(Component.translatable("snbt.parser.expected_number_or_boolean"))
   );
   public static final String BUILTIN_TRUE = "true";
   public static final String BUILTIN_FALSE = "false";
   public static final Map<SnbtOperations$BuiltinKey, SnbtOperations$BuiltinOperation> BUILTIN_OPERATIONS = Map.of(
      new SnbtOperations$BuiltinKey("bool", 1), new SnbtOperations$1(), new SnbtOperations$BuiltinKey("uuid", 1), new SnbtOperations$2()
   );
   public static final SuggestionSupplier<StringReader> BUILTIN_IDS = new SnbtOperations$3();
}
