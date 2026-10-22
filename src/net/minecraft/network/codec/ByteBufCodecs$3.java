package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;

class ByteBufCodecs$3 implements StreamCodec<ByteBuf, Short> {
   public Short decode(final ByteBuf input) {
      return input.readShort();
   }

   public void encode(final ByteBuf output, final Short value) {
      output.writeShort(value);
   }
}
