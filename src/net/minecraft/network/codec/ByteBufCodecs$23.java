package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import org.joml.Quaternionfc;

class ByteBufCodecs$23 implements StreamCodec<ByteBuf, Quaternionfc> {
   public Quaternionfc decode(final ByteBuf input) {
      return FriendlyByteBuf.readQuaternion(input);
   }

   public void encode(final ByteBuf output, final Quaternionfc value) {
      FriendlyByteBuf.writeQuaternion(output, value);
   }
}
