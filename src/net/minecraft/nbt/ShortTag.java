package net.minecraft.nbt;

import java.io.DataOutput;
import java.io.IOException;

public record ShortTag(short value) implements NumericTag {
   private static final int SELF_SIZE_IN_BYTES = 10;
   public static final TagType<ShortTag> TYPE = new ShortTag$1();

   @Deprecated(
      forRemoval = true
   )
   public ShortTag(short value) {
      this.value = value;
   }

   public static ShortTag valueOf(final short i) {
      return i >= -128 && i <= 1024 ? ShortTag$Cache.cache[i - -128] : new ShortTag(i);
   }

   @Override
   public void write(final DataOutput output) throws IOException {
      output.writeShort(this.value);
   }

   @Override
   public int sizeInBytes() {
      return 10;
   }

   @Override
   public byte getId() {
      return 2;
   }

   @Override
   public TagType<ShortTag> getType() {
      return TYPE;
   }

   public ShortTag copy() {
      return this;
   }

   @Override
   public void accept(final TagVisitor visitor) {
      visitor.visitShort(this);
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
      return (byte)(this.value & 255);
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
      visitor.visitShort(this);
      return visitor.build();
   }
}
