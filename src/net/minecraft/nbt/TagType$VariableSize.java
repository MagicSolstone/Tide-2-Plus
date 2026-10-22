package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;

public interface TagType$VariableSize<T extends Tag> extends TagType<T> {
   @Override
   default void skip(final DataInput input, final int count, final NbtAccounter accounter) throws IOException {
      for (int i = 0; i < count; i++) {
         this.skip(input, accounter);
      }
   }
}
