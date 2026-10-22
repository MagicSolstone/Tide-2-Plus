package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;

class ByteBufCodecs$24 implements StreamCodec<ByteBuf, Integer> {
   public Integer decode(final ByteBuf input) {
      return FriendlyByteBuf.readContainerId(input);
   }

   public void encode(final ByteBuf output, final Integer value) {
      FriendlyByteBuf.writeContainerId(output, value);
   }
}
