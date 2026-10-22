package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;

class TagType$1 implements TagType<EndTag> {
   TagType$1(final int val$id) {
      this.val$id = val$idx;
   }

   private IOException createException() {
      return new IOException("Invalid tag id: " + this.val$id);
   }

   public EndTag load(final DataInput input, final NbtAccounter accounter) throws IOException {
      throw this.createException();
   }

   @Override
   public StreamTagVisitor$ValueResult parse(final DataInput input, final StreamTagVisitor output, final NbtAccounter accounter) throws IOException {
      throw this.createException();
   }

   @Override
   public void skip(final DataInput input, final int count, final NbtAccounter accounter) throws IOException {
      throw this.createException();
   }

   @Override
   public void skip(final DataInput input, final NbtAccounter accounter) throws IOException {
      throw this.createException();
   }

   @Override
   public String getName() {
      return "INVALID[" + this.val$id + "]";
   }

   @Override
   public String getPrettyName() {
      return "UNKNOWN_" + this.val$id;
   }
}
