package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;

class ByteBufCodecs$2 implements StreamCodec<ByteBuf, Byte> {
   public Byte decode(final ByteBuf input) {
      return input.readByte();
   }

   public void encode(final ByteBuf output, final Byte value) {
      output.writeByte(value);
   }
}
