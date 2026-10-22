package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.ARGB;

class ByteBufCodecs$36 implements StreamCodec<ByteBuf, Integer> {
   public Integer decode(final ByteBuf input) {
      return ARGB.color(input.readByte() & 0xFF, input.readByte() & 0xFF, input.readByte() & 0xFF);
   }

   public void encode(final ByteBuf output, final Integer value) {
      output.writeByte(ARGB.red(value));
      output.writeByte(ARGB.green(value));
      output.writeByte(ARGB.blue(value));
   }
}
