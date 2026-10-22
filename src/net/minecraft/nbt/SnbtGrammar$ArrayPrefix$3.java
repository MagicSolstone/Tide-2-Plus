package net.minecraft.nbt;

import com.mojang.serialization.DynamicOps;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.LongStream.Builder;
import net.minecraft.util.parsing.packrat.ParseState;
import org.jspecify.annotations.Nullable;

enum SnbtGrammar$ArrayPrefix$3 {
   private SnbtGrammar$ArrayPrefix$3(final SnbtGrammar$TypeSuffix defaultType, final SnbtGrammar$TypeSuffix... additionalTypes) {
   }

   @Override
   public <T> T create(final DynamicOps<T> ops) {
      return (T)ops.createLongList(LongStream.empty());
   }

   @Nullable
   @Override
   public <T> T create(final DynamicOps<T> ops, final List<SnbtGrammar$IntegerLiteral> entries, final ParseState<?> state) {
      Builder result = LongStream.builder();

      for (SnbtGrammar$IntegerLiteral entry : entries) {
         Number parsedNumber = this.buildNumber(entry, state);
         if (parsedNumber == null) {
            return null;
         }

         result.add(parsedNumber.longValue());
      }

      return (T)ops.createLongList(result.build());
   }
}
