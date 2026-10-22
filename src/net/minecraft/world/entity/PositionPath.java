package net.minecraft.world.entity;

import io.netty.buffer.ByteBuf;
import java.util.List;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public sealed interface PositionPath permits PositionPath$Linear, PositionPath$Stepped {
   StreamCodec<ByteBuf, PositionPath> STREAM_CODEC = PositionPath$Type.STREAM_CODEC.dispatch(PositionPath::type, PositionPath$Type::streamCodec);

   Vec3 endPosition();

   PositionPath$Type type();

   static PositionPath of(final Vec3 position) {
      return new PositionPath$Linear(position);
   }

   static PositionPath stepped(final List<PositionStep> steps) {
      return new PositionPath$Stepped(steps);
   }
}
