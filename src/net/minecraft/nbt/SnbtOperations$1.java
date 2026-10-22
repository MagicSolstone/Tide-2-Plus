package net.minecraft.nbt;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.DynamicOps;
import java.util.List;
import java.util.Optional;
import net.minecraft.util.parsing.packrat.ParseState;
import org.jspecify.annotations.Nullable;

class SnbtOperations$1 implements SnbtOperations$BuiltinOperation {
   @Override
   public <T> T run(final DynamicOps<T> ops, final List<T> arguments, final ParseState<StringReader> state) {
      Boolean result = convert(ops, arguments.getFirst());
      if (result == null) {
         state.errorCollector().store(state.mark(), SnbtOperations.ERROR_EXPECTED_NUMBER_OR_BOOLEAN);
         return null;
      } else {
         return (T)ops.createBoolean(result);
      }
   }

   @Nullable
   private static <T> Boolean convert(final DynamicOps<T> ops, final T arg) {
      Optional<Boolean> asBoolean = ops.getBooleanValue(arg).result();
      if (asBoolean.isPresent()) {
         return asBoolean.get();
      } else {
         Optional<Number> asNumber = ops.getNumberValue(arg).result();
         return asNumber.isPresent() ? asNumber.get().doubleValue() != 0.0 : null;
      }
   }
}
