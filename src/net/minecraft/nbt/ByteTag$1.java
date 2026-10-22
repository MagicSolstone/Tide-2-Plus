package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;

class ByteTag$1 implements TagType$StaticSize<ByteTag> {
   public ByteTag load(final DataInput input, final NbtAccounter accounter) throws IOException {
      return ByteTag.valueOf(readAccounted(input, accounter));
   }

   @Override
   public StreamTagVisitor$ValueResult parse(final DataInput input, final StreamTagVisitor output, final NbtAccounter accounter) throws IOException {
      return output.visit(readAccounted(input, accounter));
   }

   private static byte readAccounted(final DataInput input, final NbtAccounter accounter) throws IOException {
      accounter.accountBytes(9L);
      return input.readByte();
   }

   @Override
   public int size() {
      return 1;
   }

   @Override
   public String getName() {
      return "BYTE";
   }

   @Override
   public String getPrettyName() {
      return "TAG_Byte";
   }
}
