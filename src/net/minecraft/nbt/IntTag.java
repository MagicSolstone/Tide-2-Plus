package net.minecraft.nbt;

import java.io.DataOutput;
import java.io.IOException;

public record IntTag(int value) implements NumericTag {
   private static final int SELF_SIZE_IN_BYTES = 12;
   public static final TagType<IntTag> TYPE = new IntTag$1();

   @Deprecated(
      forRemoval = true
   )
   public IntTag(int value) {
      this.value = value;
   }

   public static IntTag valueOf(final int i) {
      return i >= -128 && i <= 1024 ? IntTag$Cache.cache[i - -128] : new IntTag(i);
   }

   @Override
   public void write(final DataOutput output) throws IOException {
      output.writeInt(this.value);
   }

   @Override
   public int sizeInBytes() {
      return 12;
   }

   @Override
   public byte getId() {
      return 3;
   }

   @Override
   public TagType<IntTag> getType() {
      return TYPE;
   }

   public IntTag copy() {
      return this;
   }

   @Override
   public void accept(final TagVisitor visitor) {
      visitor.visitInt(this);
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
      return (short)(this.value & 65535);
   }

   @Override
   public byte byteValue() {
      return (byte)(this.value & 0xFF);
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
      visitor.visitInt(this);
      return visitor.build();
   }
}
