package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;

class ByteBufCodecs$13 implements StreamCodec<ByteBuf, long[]> {
   public long[] decode(final ByteBuf input) {
      return FriendlyByteBuf.readLongArray(input);
   }

   public void encode(final ByteBuf output, final long[] value) {
      FriendlyByteBuf.writeLongArray(output, value);
   }
}
