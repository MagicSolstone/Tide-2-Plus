package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;
import java.util.BitSet;
import net.minecraft.network.FriendlyByteBuf;

class ByteBufCodecs$15 implements StreamCodec<ByteBuf, BitSet> {
   public BitSet decode(final ByteBuf input) {
      return BitSet.valueOf(FriendlyByteBuf.readByteArray(input));
   }

   public void encode(final ByteBuf output, final BitSet value) {
      FriendlyByteBuf.writeByteArray(output, value.toByteArray());
   }
}
