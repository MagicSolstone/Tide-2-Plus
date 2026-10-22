package net.minecraft.nbt;

import java.io.DataOutput;
import java.io.IOException;

public final class EndTag implements Tag {
   private static final int SELF_SIZE_IN_BYTES = 8;
   public static final TagType<EndTag> TYPE = new EndTag$1();
   public static final EndTag INSTANCE = new EndTag();

   private EndTag() {
   }

   @Override
   public void write(final DataOutput output) throws IOException {
   }

   @Override
   public int sizeInBytes() {
      return 8;
   }

   @Override
   public byte getId() {
      return 0;
   }

   @Override
   public TagType<EndTag> getType() {
      return TYPE;
   }

   @Override
   public String toString() {
      StringTagVisitor visitor = new StringTagVisitor();
      visitor.visitEnd(this);
      return visitor.build();
   }

   public EndTag copy() {
      return this;
   }

   @Override
   public void accept(final TagVisitor visitor) {
      visitor.visitEnd(this);
   }

   @Override
   public StreamTagVisitor$ValueResult accept(final StreamTagVisitor visitor) {
      return visitor.visitEnd();
   }
}
