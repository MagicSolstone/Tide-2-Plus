package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;
import java.util.BitSet;
import net.minecraft.network.FriendlyByteBuf;

class ByteBufCodecs$16 implements StreamCodec<ByteBuf, BitSet> {
   ByteBufCodecs$16(final int val$size) {
      this.val$size = val$sizex;
   }

   public BitSet decode(final ByteBuf input) {
      return FriendlyByteBuf.readFixedBitSet(input, this.val$size);
   }

   public void encode(final ByteBuf output, final BitSet value) {
      FriendlyByteBuf.writeFixedBitSet(output, value, this.val$size);
   }
}
