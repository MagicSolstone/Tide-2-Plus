package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.VarLong;

class ByteBufCodecs$8 implements StreamCodec<ByteBuf, Long> {
   public Long decode(final ByteBuf input) {
      return VarLong.read(input);
   }

   public void encode(final ByteBuf output, final Long value) {
      VarLong.write(output, value);
   }
}
