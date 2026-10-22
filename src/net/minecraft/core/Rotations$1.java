package net.minecraft.core;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

class Rotations$1 implements StreamCodec<ByteBuf, Rotations> {
   public Rotations decode(final ByteBuf input) {
      return new Rotations(input.readFloat(), input.readFloat(), input.readFloat());
   }

   public void encode(final ByteBuf output, final Rotations value) {
      output.writeFloat(value.x);
      output.writeFloat(value.y);
      output.writeFloat(value.z);
   }
}
