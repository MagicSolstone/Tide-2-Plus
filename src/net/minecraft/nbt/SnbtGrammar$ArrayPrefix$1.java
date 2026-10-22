package net.minecraft.nbt;

import com.mojang.serialization.DynamicOps;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.bytes.ByteList;
import java.nio.ByteBuffer;
import java.util.List;
import net.minecraft.util.parsing.packrat.ParseState;
import org.jspecify.annotations.Nullable;

enum SnbtGrammar$ArrayPrefix$1 {
   private static final ByteBuffer EMPTY_BUFFER = ByteBuffer.wrap(new byte[0]);

   private SnbtGrammar$ArrayPrefix$1(final SnbtGrammar$TypeSuffix defaultType, final SnbtGrammar$TypeSuffix... additionalTypes) {
   }

   @Override
   public <T> T create(final DynamicOps<T> ops) {
      return (T)ops.createByteList(EMPTY_BUFFER);
   }

   @Nullable
   @Override
   public <T> T create(final DynamicOps<T> ops, final List<SnbtGrammar$IntegerLiteral> entries, final ParseState<?> state) {
      ByteList result = new ByteArrayList();

      for (SnbtGrammar$IntegerLiteral entry : entries) {
         Number parsedNumber = this.buildNumber(entry, state);
         if (parsedNumber == null) {
            return null;
         }

         result.add(parsedNumber.byteValue());
      }

      return (T)ops.createByteList(ByteBuffer.wrap(result.toByteArray()));
   }
}
