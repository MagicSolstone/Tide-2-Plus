package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;

class ShortTag$1 implements TagType$StaticSize<ShortTag> {
   public ShortTag load(final DataInput input, final NbtAccounter accounter) throws IOException {
      return ShortTag.valueOf(readAccounted(input, accounter));
   }

   @Override
   public StreamTagVisitor$ValueResult parse(final DataInput input, final StreamTagVisitor output, final NbtAccounter accounter) throws IOException {
      return output.visit(readAccounted(input, accounter));
   }

   private static short readAccounted(final DataInput input, final NbtAccounter accounter) throws IOException {
      accounter.accountBytes(10L);
      return input.readShort();
   }

   @Override
   public int size() {
      return 2;
   }

   @Override
   public String getName() {
      return "SHORT";
   }

   @Override
   public String getPrettyName() {
      return "TAG_Short";
   }
}
