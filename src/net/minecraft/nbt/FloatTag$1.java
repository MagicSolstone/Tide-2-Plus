package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;

class FloatTag$1 implements TagType$StaticSize<FloatTag> {
   public FloatTag load(final DataInput input, final NbtAccounter accounter) throws IOException {
      return FloatTag.valueOf(readAccounted(input, accounter));
   }

   @Override
   public StreamTagVisitor$ValueResult parse(final DataInput input, final StreamTagVisitor output, final NbtAccounter accounter) throws IOException {
      return output.visit(readAccounted(input, accounter));
   }

   private static float readAccounted(final DataInput input, final NbtAccounter accounter) throws IOException {
      accounter.accountBytes(12L);
      return input.readFloat();
   }

   @Override
   public int size() {
      return 4;
   }

   @Override
   public String getName() {
      return "FLOAT";
   }

   @Override
   public String getPrettyName() {
      return "TAG_Float";
   }
}
