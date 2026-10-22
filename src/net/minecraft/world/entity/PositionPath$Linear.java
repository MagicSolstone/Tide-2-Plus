package net.minecraft.world.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public record PositionPath$Linear(Vec3 endPosition) implements PositionPath {
   public static final StreamCodec<ByteBuf, PositionPath$Linear> STREAM_CODEC = Vec3.STREAM_CODEC
      .map(PositionPath$Linear::new, PositionPath$Linear::endPosition);

   @Override
   public PositionPath$Type type() {
      return PositionPath$Type.LINEAR;
   }
}
