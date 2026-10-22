package net.minecraft.core;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

class BlockPos$1 implements StreamCodec<ByteBuf, BlockPos> {
   public BlockPos decode(final ByteBuf input) {
      return FriendlyByteBuf.readBlockPos(input);
   }

   public void encode(final ByteBuf output, final BlockPos value) {
      FriendlyByteBuf.writeBlockPos(output, value);
   }
}
