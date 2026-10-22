package net.minecraft.world.level;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

class ChunkPos$1 implements StreamCodec<ByteBuf, ChunkPos> {
   public ChunkPos decode(final ByteBuf input) {
      return FriendlyByteBuf.readChunkPos(input);
   }

   public void encode(final ByteBuf output, final ChunkPos value) {
      FriendlyByteBuf.writeChunkPos(output, value);
   }
}
