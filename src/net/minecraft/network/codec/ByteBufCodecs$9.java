package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;

class ByteBufCodecs$9 implements StreamCodec<ByteBuf, Float> {
   public Float decode(final ByteBuf input) {
      return input.readFloat();
   }

   public void encode(final ByteBuf output, final Float value) {
      output.writeFloat(value);
   }
}
