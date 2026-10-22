package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;

class IntTag$1 implements TagType$StaticSize<IntTag> {
   public IntTag load(final DataInput input, final NbtAccounter accounter) throws IOException {
      return IntTag.valueOf(readAccounted(input, accounter));
   }

   @Override
   public StreamTagVisitor$ValueResult parse(final DataInput input, final StreamTagVisitor output, final NbtAccounter accounter) throws IOException {
      return output.visit(readAccounted(input, accounter));
   }

   private static int readAccounted(final DataInput input, final NbtAccounter accounter) throws IOException {
      accounter.accountBytes(12L);
      return input.readInt();
   }

   @Override
   public int size() {
      return 4;
   }

   @Override
   public String getName() {
      return "INT";
   }

   @Override
   public String getPrettyName() {
      return "TAG_Int";
   }
}
