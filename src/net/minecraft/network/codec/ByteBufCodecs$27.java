package net.minecraft.network.codec;

import io.netty.handler.codec.EncoderException;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.IntFunction;

class ByteBufCodecs$27 implements StreamCodec<B, C> {
   ByteBufCodecs$27(final IntFunction val$constructor, final int val$size, final StreamCodec val$elementCodec) {
      this.val$constructor = val$constructorx;
      this.val$size = val$sizex;
      this.val$elementCodec = val$elementCodecx;
   }

   public C decode(final B input) {
      C result = (C)((Collection)this.val$constructor.apply(this.val$size));

      for (int i = 0; i < this.val$size; i++) {
         result.add(this.val$elementCodec.decode(input));
      }

      return result;
   }

   public void encode(final B output, final C value) {
      if (value.size() != this.val$size) {
         throw new EncoderException("Invalid list size, expected " + this.val$size + ", but got " + value.size());
      } else {
         Iterator<V> iterator = value.iterator();

         for (int i = 0; i < this.val$size; i++) {
            this.val$elementCodec.encode(output, iterator.next());
         }
      }
   }
}
