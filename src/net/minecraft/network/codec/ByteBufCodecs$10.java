package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;

class ByteBufCodecs$10 implements StreamCodec<ByteBuf, Double> {
   public Double decode(final ByteBuf input) {
      return input.readDouble();
   }

   public void encode(final ByteBuf output, final Double value) {
      output.writeDouble(value);
   }
}
