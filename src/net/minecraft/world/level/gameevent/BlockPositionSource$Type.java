package net.minecraft.world.level.gameevent;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class BlockPositionSource$Type implements PositionSourceType<BlockPositionSource> {
   @Override
   public MapCodec<BlockPositionSource> codec() {
      return BlockPositionSource.CODEC;
   }

   @Override
   public StreamCodec<ByteBuf, BlockPositionSource> streamCodec() {
      return BlockPositionSource.STREAM_CODEC;
   }
}
