package net.minecraft.core;

import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

class UUIDUtil$1 implements StreamCodec<ByteBuf, UUID> {
   public UUID decode(final ByteBuf input) {
      return FriendlyByteBuf.readUUID(input);
   }

   public void encode(final ByteBuf output, final UUID value) {
      FriendlyByteBuf.writeUUID(output, value);
   }
}
