package net.minecraft.nbt;

import java.io.DataOutput;
import java.io.IOException;

public record LongTag(long value) implements NumericTag {
   private static final int SELF_SIZE_IN_BYTES = 16;
   public static final TagType<LongTag> TYPE = new LongTag$1();

   @Deprecated(
      forRemoval = true
   )
   public LongTag(long value) {
      this.value = value;
   }

   public static LongTag valueOf(final long i) {
      return i >= -128L && i <= 1024L ? LongTag$Cache.cache[(int)i - -128] : new LongTag(i);
   }

   @Override
   public void write(final DataOutput output) throws IOException {
      output.writeLong(this.value);
   }

   @Override
   public int sizeInBytes() {
      return 16;
   }

   @Override
   public byte getId() {
      return 4;
   }

   @Override
   public TagType<LongTag> getType() {
      return TYPE;
   }

   public LongTag copy() {
      return this;
   }

   @Override
   public void accept(final TagVisitor visitor) {
      visitor.visitLong(this);
   }

   @Override
   public long longValue() {
      return this.value;
   }

   @Override
   public int intValue() {
      return (int)(this.value & -1L);
   }

   @Override
   public short shortValue() {
      return (short)(this.value & 65535L);
   }

   @Override
   public byte byteValue() {
      return (byte)(this.value & 255L);
   }

   @Override
   public double doubleValue() {
      return this.value;
   }

   @Override
   public float floatValue() {
      return (float)this.value;
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
      visitor.visitLong(this);
      return visitor.build();
   }
}
