package net.minecraft.nbt;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.DynamicOps;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;
import net.minecraft.core.UUIDUtil;
import net.minecraft.util.parsing.packrat.ParseState;

class SnbtOperations$2 implements SnbtOperations$BuiltinOperation {
   @Override
   public <T> T run(final DynamicOps<T> ops, final List<T> arguments, final ParseState<StringReader> state) {
      Optional<String> arg = ops.getStringValue(arguments.getFirst()).result();
      if (arg.isEmpty()) {
         state.errorCollector().store(state.mark(), SnbtOperations.ERROR_EXPECTED_STRING_UUID);
         return null;
      } else {
         UUID uuid;
         try {
            uuid = UUID.fromString(arg.get());
         } catch (IllegalArgumentException var7) {
            state.errorCollector().store(state.mark(), SnbtOperations.ERROR_EXPECTED_STRING_UUID);
            return null;
         }

         return (T)ops.createIntList(IntStream.of(UUIDUtil.uuidToIntArray(uuid)));
      }
   }
}
