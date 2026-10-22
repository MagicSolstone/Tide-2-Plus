package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.VarInt;

class ByteBufCodecs$6 implements StreamCodec<ByteBuf, Integer> {
   public Integer decode(final ByteBuf input) {
      return VarInt.read(input);
   }

   public void encode(final ByteBuf output, final Integer value) {
      VarInt.write(output, value);
   }
}
