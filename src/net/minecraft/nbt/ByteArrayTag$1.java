package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;

class ByteArrayTag$1 implements TagType$VariableSize<ByteArrayTag> {
   public ByteArrayTag load(final DataInput input, final NbtAccounter accounter) throws IOException {
      return new ByteArrayTag(readAccounted(input, accounter));
   }

   @Override
   public StreamTagVisitor$ValueResult parse(final DataInput input, final StreamTagVisitor output, final NbtAccounter accounter) throws IOException {
      return output.visit(readAccounted(input, accounter));
   }

   private static byte[] readAccounted(final DataInput input, final NbtAccounter accounter) throws IOException {
      accounter.accountBytes(24L);
      int length = input.readInt();
      accounter.accountBytes(1L, length);
      byte[] data = new byte[length];
      input.readFully(data);
      return data;
   }

   @Override
   public void skip(final DataInput input, final NbtAccounter accounter) throws IOException {
      input.skipBytes(input.readInt() * 1);
   }

   @Override
   public String getName() {
      return "BYTE[]";
   }

   @Override
   public String getPrettyName() {
      return "TAG_Byte_Array";
   }
}
