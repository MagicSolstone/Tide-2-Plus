package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import org.joml.Vector3fc;

class ByteBufCodecs$22 implements StreamCodec<ByteBuf, Vector3fc> {
   public Vector3fc decode(final ByteBuf input) {
      return FriendlyByteBuf.readVector3f(input);
   }

   public void encode(final ByteBuf output, final Vector3fc value) {
      FriendlyByteBuf.writeVector3f(output, value);
   }
}
