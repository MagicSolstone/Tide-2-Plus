package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;

class ByteBufCodecs$4 implements StreamCodec<ByteBuf, Integer> {
   public Integer decode(final ByteBuf input) {
      return input.readUnsignedShort();
   }

   public void encode(final ByteBuf output, final Integer value) {
      output.writeShort(value);
   }
}
