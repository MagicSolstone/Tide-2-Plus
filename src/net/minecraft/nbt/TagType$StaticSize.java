package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;

public interface TagType$StaticSize<T extends Tag> extends TagType<T> {
   @Override
   default void skip(final DataInput input, final NbtAccounter accounter) throws IOException {
      input.skipBytes(this.size());
   }

   @Override
   default void skip(final DataInput input, final int count, final NbtAccounter accounter) throws IOException {
      input.skipBytes(this.size() * count);
   }

   int size();
}
