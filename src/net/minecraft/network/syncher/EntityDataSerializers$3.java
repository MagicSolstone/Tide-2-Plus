package net.minecraft.network.syncher;

import io.netty.buffer.ByteBuf;
import java.util.OptionalInt;
import net.minecraft.network.VarInt;
import net.minecraft.network.codec.StreamCodec;

class EntityDataSerializers$3 implements StreamCodec<ByteBuf, OptionalInt> {
   public OptionalInt decode(final ByteBuf input) {
      int v = VarInt.read(input);
      return v == 0 ? OptionalInt.empty() : OptionalInt.of(v - 1);
   }

   public void encode(final ByteBuf output, final OptionalInt value) {
      VarInt.write(output, value.orElse(-1) + 1);
   }
}
