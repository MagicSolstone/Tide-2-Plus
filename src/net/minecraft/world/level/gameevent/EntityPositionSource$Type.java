package net.minecraft.world.level.gameevent;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class EntityPositionSource$Type implements PositionSourceType<EntityPositionSource> {
   @Override
   public MapCodec<EntityPositionSource> codec() {
      return EntityPositionSource.CODEC;
   }

   @Override
   public StreamCodec<ByteBuf, EntityPositionSource> streamCodec() {
      return EntityPositionSource.STREAM_CODEC;
   }
}
