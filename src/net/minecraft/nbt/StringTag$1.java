package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;

class StringTag$1 implements TagType$VariableSize<StringTag> {
   public StringTag load(final DataInput input, final NbtAccounter accounter) throws IOException {
      return StringTag.valueOf(readAccounted(input, accounter));
   }

   @Override
   public StreamTagVisitor$ValueResult parse(final DataInput input, final StreamTagVisitor output, final NbtAccounter accounter) throws IOException {
      return output.visit(readAccounted(input, accounter));
   }

   private static String readAccounted(final DataInput input, final NbtAccounter accounter) throws IOException {
      accounter.accountBytes(36L);
      String data = input.readUTF();
      accounter.accountBytes(2L, data.length());
      return data;
   }

   @Override
   public void skip(final DataInput input, final NbtAccounter accounter) throws IOException {
      StringTag.skipString(input);
   }

   @Override
   public String getName() {
      return "STRING";
   }

   @Override
   public String getPrettyName() {
      return "TAG_String";
   }
}
