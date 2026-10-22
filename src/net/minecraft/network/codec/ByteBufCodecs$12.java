package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;

class ByteBufCodecs$12 implements StreamCodec<ByteBuf, byte[]> {
   public byte[] decode(final ByteBuf input) {
      return FriendlyByteBuf.readByteArray(input);
   }

   public void encode(final ByteBuf output, final byte[] value) {
      FriendlyByteBuf.writeByteArray(output, value);
   }
}
