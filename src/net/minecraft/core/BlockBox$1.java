package net.minecraft.core;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

class BlockBox$1 implements StreamCodec<ByteBuf, BlockBox> {
   public BlockBox decode(final ByteBuf input) {
      return new BlockBox(FriendlyByteBuf.readBlockPos(input), FriendlyByteBuf.readBlockPos(input));
   }

   public void encode(final ByteBuf output, final BlockBox value) {
      FriendlyByteBuf.writeBlockPos(output, value.min());
      FriendlyByteBuf.writeBlockPos(output, value.max());
   }
}
