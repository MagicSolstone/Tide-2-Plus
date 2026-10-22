package net.minecraft.nbt;

import java.io.DataOutput;
import java.io.IOException;

public record ByteTag(byte value) implements NumericTag {
   private static final int SELF_SIZE_IN_BYTES = 9;
   public static final TagType<ByteTag> TYPE = new ByteTag$1();
   public static final ByteTag ZERO = valueOf((byte)0);
   public static final ByteTag ONE = valueOf((byte)1);

   @Deprecated(
      forRemoval = true
   )
   public ByteTag(byte value) {
      this.value = value;
   }

   public static ByteTag valueOf(final byte data) {
      return ByteTag$Cache.cache[128 + data];
   }

   public static ByteTag valueOf(final boolean data) {
      return data ? ONE : ZERO;
   }

   @Override
   public void write(final DataOutput output) throws IOException {
      output.writeByte(this.value);
   }

   @Override
   public int sizeInBytes() {
      return 9;
   }

   @Override
   public byte getId() {
      return 1;
   }

   @Override
   public TagType<ByteTag> getType() {
      return TYPE;
   }

   public ByteTag copy() {
      return this;
   }

   @Override
   public void accept(final TagVisitor visitor) {
      visitor.visitByte(this);
   }

   @Override
   public long longValue() {
      return this.value;
   }

   @Override
   public int intValue() {
      return this.value;
   }

   @Override
   public short shortValue() {
      return this.value;
   }

   @Override
   public byte byteValue() {
      return this.value;
   }

   @Override
   public double doubleValue() {
      return this.value;
   }

   @Override
   public float floatValue() {
      return this.value;
   }

   @Override
   public Number box() {
      return this.value;
   }

   @Override
   public StreamTagVisitor$ValueResult accept(final StreamTagVisitor visitor) {
      return visitor.visit(this.value);
   }

   @Override
   public String toString() {
      StringTagVisitor visitor = new StringTagVisitor();
      visitor.visitByte(this);
      return visitor.build();
   }
}
