package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;

class ByteBufCodecs$5 implements StreamCodec<ByteBuf, Integer> {
   public Integer decode(final ByteBuf input) {
      return input.readInt();
   }

   public void encode(final ByteBuf output, final Integer value) {
      output.writeInt(value);
   }
}
