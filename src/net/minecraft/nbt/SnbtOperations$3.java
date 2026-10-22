package net.minecraft.nbt;

import com.mojang.brigadier.StringReader;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.util.parsing.packrat.ParseState;
import net.minecraft.util.parsing.packrat.SuggestionSupplier;

class SnbtOperations$3 implements SuggestionSupplier<StringReader> {
   private final Set<String> keys = Stream.concat(
         Stream.of("false", "true"), SnbtOperations.BUILTIN_OPERATIONS.keySet().stream().map(SnbtOperations$BuiltinKey::id)
      )
      .collect(Collectors.toSet());

   @Override
   public Stream<String> possibleValues(final ParseState<StringReader> state) {
      return this.keys.stream();
   }
}
