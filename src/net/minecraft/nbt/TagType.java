package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;

public interface TagType<T extends Tag> {
   T load(DataInput input, NbtAccounter accounter) throws IOException;

   StreamTagVisitor$ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException;

   default void parseRoot(final DataInput input, final StreamTagVisitor output, final NbtAccounter accounter) throws IOException {
      switch (output.visitRootEntry(this)) {
         case CONTINUE:
            this.parse(input, output, accounter);
         case HALT:
         default:
            break;
         case BREAK:
            this.skip(input, accounter);
      }
   }

   void skip(DataInput input, int count, NbtAccounter accounter) throws IOException;

   void skip(DataInput input, NbtAccounter accounter) throws IOException;

   String getName();

   String getPrettyName();

   static TagType<EndTag> createInvalid(final int id) {
      return new TagType$1(id);
   }
}
