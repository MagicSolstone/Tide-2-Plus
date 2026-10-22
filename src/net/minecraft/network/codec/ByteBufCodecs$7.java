package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;

class ByteBufCodecs$7 implements StreamCodec<ByteBuf, Long> {
   public Long decode(final ByteBuf input) {
      return input.readLong();
   }

   public void encode(final ByteBuf output, final Long value) {
      output.writeLong(value);
   }
}
