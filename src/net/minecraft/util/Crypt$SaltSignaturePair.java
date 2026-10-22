package net.minecraft.util;

import com.google.common.primitives.Longs;
import it.unimi.dsi.fastutil.bytes.ByteArrays;
import net.minecraft.network.FriendlyByteBuf;

public record Crypt$SaltSignaturePair(long salt, byte[] signature) {
   public static final Crypt$SaltSignaturePair EMPTY = new Crypt$SaltSignaturePair(0L, ByteArrays.EMPTY_ARRAY);

   public Crypt$SaltSignaturePair(final FriendlyByteBuf input) {
      this(input.readLong(), input.readByteArray());
   }

   public boolean isValid() {
      return this.signature.length > 0;
   }

   public static void write(final FriendlyByteBuf output, final Crypt$SaltSignaturePair saltSignaturePair) {
      output.writeLong(saltSignaturePair.salt);
      output.writeByteArray(saltSignaturePair.signature);
   }

   public byte[] saltAsBytes() {
      return Longs.toByteArray(this.salt);
   }
}
