package net.minecraft.network.codec;

import java.util.Collection;
import java.util.function.IntFunction;

class ByteBufCodecs$26 implements StreamCodec<B, C> {
   ByteBufCodecs$26(final int val$maxSize, final IntFunction val$constructor, final StreamCodec val$elementCodec) {
      this.val$maxSize = val$maxSizex;
      this.val$constructor = val$constructorx;
      this.val$elementCodec = val$elementCodecx;
   }

   public C decode(final B input) {
      int count = ByteBufCodecs.readCount(input, this.val$maxSize);
      C result = (C)((Collection)this.val$constructor.apply(Math.min(count, 65536)));

      for (int i = 0; i < count; i++) {
         result.add(this.val$elementCodec.decode(input));
      }

      return result;
   }

   public void encode(final B output, final C value) {
      ByteBufCodecs.writeCount(output, value.size(), this.val$maxSize);

      for (V element : value) {
         this.val$elementCodec.encode(output, element);
      }
   }
}
