package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;
import net.minecraft.network.VarInt;

class ByteBufCodecs$31 implements StreamCodec<ByteBuf, T> {
   ByteBufCodecs$31(final IntFunction val$byId, final ToIntFunction val$toId) {
      this.val$byId = val$byIdx;
      this.val$toId = val$toIdx;
   }

   public T decode(final ByteBuf input) {
      int id = VarInt.read(input);
      return (T)this.val$byId.apply(id);
   }

   public void encode(final ByteBuf output, final T value) {
      int id = this.val$toId.applyAsInt(value);
      VarInt.write(output, id);
   }
}
