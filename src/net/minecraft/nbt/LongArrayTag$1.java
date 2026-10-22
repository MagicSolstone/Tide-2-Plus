package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;

class LongArrayTag$1 implements TagType$VariableSize<LongArrayTag> {
   public LongArrayTag load(final DataInput input, final NbtAccounter accounter) throws IOException {
      return new LongArrayTag(readAccounted(input, accounter));
   }

   @Override
   public StreamTagVisitor$ValueResult parse(final DataInput input, final StreamTagVisitor output, final NbtAccounter accounter) throws IOException {
      return output.visit(readAccounted(input, accounter));
   }

   private static long[] readAccounted(final DataInput input, final NbtAccounter accounter) throws IOException {
      accounter.accountBytes(24L);
      int length = input.readInt();
      accounter.accountBytes(8L, length);
      long[] data = new long[length];

      for (int i = 0; i < length; i++) {
         data[i] = input.readLong();
      }

      return data;
   }

   @Override
   public void skip(final DataInput input, final NbtAccounter accounter) throws IOException {
      input.skipBytes(input.readInt() * 8);
   }

   @Override
   public String getName() {
      return "LONG[]";
   }

   @Override
   public String getPrettyName() {
      return "TAG_Long_Array";
   }
}
