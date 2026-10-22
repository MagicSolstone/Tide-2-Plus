package net.minecraft.nbt;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import org.apache.commons.lang3.ArrayUtils;

public final class ByteArrayTag implements CollectionTag {
   private static final int SELF_SIZE_IN_BYTES = 24;
   public static final TagType<ByteArrayTag> TYPE = new ByteArrayTag$1();
   private byte[] data;

   public ByteArrayTag(final byte[] data) {
      this.data = data;
   }

   @Override
   public void write(final DataOutput output) throws IOException {
      output.writeInt(this.data.length);
      output.write(this.data);
   }

   @Override
   public int sizeInBytes() {
      return 24 + 1 * this.data.length;
   }

   @Override
   public byte getId() {
      return 7;
   }

   @Override
   public TagType<ByteArrayTag> getType() {
      return TYPE;
   }

   @Override
   public String toString() {
      StringTagVisitor visitor = new StringTagVisitor();
      visitor.visitByteArray(this);
      return visitor.build();
   }

   @Override
   public Tag copy() {
      byte[] cp = new byte[this.data.length];
      System.arraycopy(this.data, 0, cp, 0, this.data.length);
      return new ByteArrayTag(cp);
   }

   @Override
   public boolean equals(final Object obj) {
      return this == obj ? true : obj instanceof ByteArrayTag byteArrayTag && Arrays.equals(this.data, byteArrayTag.data);
   }

   @Override
   public int hashCode() {
      return Arrays.hashCode(this.data);
   }

   @Override
   public void accept(final TagVisitor visitor) {
      visitor.visitByteArray(this);
   }

   public byte[] getAsByteArray() {
      return this.data;
   }

   @Override
   public int size() {
      return this.data.length;
   }

   public ByteTag get(final int index) {
      return ByteTag.valueOf(this.data[index]);
   }

   @Override
   public boolean setTag(final int index, final Tag tag) {
      if (tag instanceof NumericTag numeric) {
         this.data[index] = numeric.byteValue();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean addTag(final int index, final Tag tag) {
      if (tag instanceof NumericTag numeric) {
         this.data = ArrayUtils.add(this.data, index, numeric.byteValue());
         return true;
      } else {
         return false;
      }
   }

   public ByteTag remove(final int index) {
      byte prev = this.data[index];
      this.data = ArrayUtils.remove(this.data, index);
      return ByteTag.valueOf(prev);
   }

   @Override
   public void clear() {
      this.data = new byte[0];
   }

   @Override
   public Optional<byte[]> asByteArray() {
      return Optional.of(this.data);
   }

   @Override
   public StreamTagVisitor$ValueResult accept(final StreamTagVisitor visitor) {
      return visitor.visit(this.data);
   }
}
