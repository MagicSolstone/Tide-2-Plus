package net.minecraft.world.phys;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

class Vec3$1 implements StreamCodec<ByteBuf, Vec3> {
   public Vec3 decode(final ByteBuf input) {
      return new Vec3(input.readDouble(), input.readDouble(), input.readDouble());
   }

   public void encode(final ByteBuf output, final Vec3 value) {
      output.writeDouble(value.x());
      output.writeDouble(value.y());
      output.writeDouble(value.z());
   }
}
