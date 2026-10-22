package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;

class DoubleTag$1 implements TagType$StaticSize<DoubleTag> {
   public DoubleTag load(final DataInput input, final NbtAccounter accounter) throws IOException {
      return DoubleTag.valueOf(readAccounted(input, accounter));
   }

   @Override
   public StreamTagVisitor$ValueResult parse(final DataInput input, final StreamTagVisitor output, final NbtAccounter accounter) throws IOException {
      return output.visit(readAccounted(input, accounter));
   }

   private static double readAccounted(final DataInput input, final NbtAccounter accounter) throws IOException {
      accounter.accountBytes(16L);
      return input.readDouble();
   }

   @Override
   public int size() {
      return 8;
   }

   @Override
   public String getName() {
      return "DOUBLE";
   }

   @Override
   public String getPrettyName() {
      return "TAG_Double";
   }
}
