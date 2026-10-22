package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;

class LongTag$1 implements TagType$StaticSize<LongTag> {
   public LongTag load(final DataInput input, final NbtAccounter accounter) throws IOException {
      return LongTag.valueOf(readAccounted(input, accounter));
   }

   @Override
   public StreamTagVisitor$ValueResult parse(final DataInput input, final StreamTagVisitor output, final NbtAccounter accounter) throws IOException {
      return output.visit(readAccounted(input, accounter));
   }

   private static long readAccounted(final DataInput input, final NbtAccounter accounter) throws IOException {
      accounter.accountBytes(16L);
      return input.readLong();
   }

   @Override
   public int size() {
      return 8;
   }

   @Override
   public String getName() {
      return "LONG";
   }

   @Override
   public String getPrettyName() {
      return "TAG_Long";
   }
}
