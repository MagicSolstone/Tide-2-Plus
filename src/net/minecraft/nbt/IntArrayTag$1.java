package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;

class IntArrayTag$1 implements TagType$VariableSize<IntArrayTag> {
   public IntArrayTag load(final DataInput input, final NbtAccounter accounter) throws IOException {
      return new IntArrayTag(readAccounted(input, accounter));
   }

   @Override
   public StreamTagVisitor$ValueResult parse(final DataInput input, final StreamTagVisitor output, final NbtAccounter accounter) throws IOException {
      return output.visit(readAccounted(input, accounter));
   }

   private static int[] readAccounted(final DataInput input, final NbtAccounter accounter) throws IOException {
      accounter.accountBytes(24L);
      int length = input.readInt();
      accounter.accountBytes(4L, length);
      int[] data = new int[length];

      for (int i = 0; i < length; i++) {
         data[i] = input.readInt();
      }

      return data;
   }

   @Override
   public void skip(final DataInput input, final NbtAccounter accounter) throws IOException {
      input.skipBytes(input.readInt() * 4);
   }

   @Override
   public String getName() {
      return "INT[]";
   }

   @Override
   public String getPrettyName() {
      return "TAG_Int_Array";
   }
}
