package net.minecraft.core;

import io.netty.buffer.ByteBuf;
import java.util.Objects;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public interface PositionAndRotation {
   StreamCodec<ByteBuf, PositionAndRotation> STREAM_CODEC = StreamCodec.composite(
      Vec3.STREAM_CODEC,
      PositionAndRotation::position,
      ByteBufCodecs.FLOAT,
      PositionAndRotation::yRot,
      ByteBufCodecs.FLOAT,
      PositionAndRotation::xRot,
      PositionAndRotation::of
   );

   Vec3 position();

   float yRot();

   float xRot();

   PositionAndRotation immutable();

   default boolean is(final Vec3 position, final float yRot, final float xRot) {
      return this.yRot() == yRot && this.xRot() == xRot && Objects.equals(this.position(), position);
   }

   static PositionAndRotation of(final Vec3 position, final float yRot, final float xRot) {
      return new PositionAndRotation$Immutable(position, yRot, xRot);
   }
}
