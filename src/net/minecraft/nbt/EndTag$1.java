package net.minecraft.nbt;

import java.io.DataInput;

class EndTag$1 implements TagType<EndTag> {
   public EndTag load(final DataInput input, final NbtAccounter accounter) {
      accounter.accountBytes(8L);
      return EndTag.INSTANCE;
   }

   @Override
   public StreamTagVisitor$ValueResult parse(final DataInput input, final StreamTagVisitor output, final NbtAccounter accounter) {
      accounter.accountBytes(8L);
      return output.visitEnd();
   }

   @Override
   public void skip(final DataInput input, final int count, final NbtAccounter accounter) {
   }

   @Override
   public void skip(final DataInput input, final NbtAccounter accounter) {
   }

   @Override
   public String getName() {
      return "END";
   }

   @Override
   public String getPrettyName() {
      return "TAG_End";
   }
}
