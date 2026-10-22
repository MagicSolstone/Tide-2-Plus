package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.EncoderException;
import net.minecraft.network.FriendlyByteBuf;

class ByteBufCodecs$11 implements StreamCodec<ByteBuf, byte[]> {
   ByteBufCodecs$11(final int val$maxSize) {
      this.val$maxSize = val$maxSizex;
   }

   public byte[] decode(final ByteBuf input) {
      return FriendlyByteBuf.readByteArray(input, this.val$maxSize);
   }

   public void encode(final ByteBuf output, final byte[] value) {
      if (value.length > this.val$maxSize) {
         throw new EncoderException("ByteArray with size " + value.length + " is bigger than allowed " + this.val$maxSize);
      } else {
         FriendlyByteBuf.writeByteArray(output, value);
      }
   }
}
