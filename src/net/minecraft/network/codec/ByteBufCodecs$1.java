package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;

class ByteBufCodecs$1 implements StreamCodec<ByteBuf, Boolean> {
   public Boolean decode(final ByteBuf input) {
      return input.readBoolean();
   }

   public void encode(final ByteBuf output, final Boolean value) {
      output.writeBoolean(value);
   }
}
