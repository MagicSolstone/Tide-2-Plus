package net.minecraft.nbt;

import com.mojang.serialization.DynamicOps;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.IntStream.Builder;
import net.minecraft.util.parsing.packrat.ParseState;
import org.jspecify.annotations.Nullable;

enum SnbtGrammar$ArrayPrefix$2 {
   private SnbtGrammar$ArrayPrefix$2(final SnbtGrammar$TypeSuffix defaultType, final SnbtGrammar$TypeSuffix... additionalTypes) {
   }

   @Override
   public <T> T create(final DynamicOps<T> ops) {
      return (T)ops.createIntList(IntStream.empty());
   }

   @Nullable
   @Override
   public <T> T create(final DynamicOps<T> ops, final List<SnbtGrammar$IntegerLiteral> entries, final ParseState<?> state) {
      Builder result = IntStream.builder();

      for (SnbtGrammar$IntegerLiteral entry : entries) {
         Number parsedNumber = this.buildNumber(entry, state);
         if (parsedNumber == null) {
            return null;
         }

         result.add(parsedNumber.intValue());
      }

      return (T)ops.createIntList(result.build());
   }
}
