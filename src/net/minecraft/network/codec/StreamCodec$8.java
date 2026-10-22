package net.minecraft.network.codec;

import java.util.function.BiFunction;
import java.util.function.Function;

class StreamCodec$8 implements StreamCodec<B, C> {
   StreamCodec$8(
      final StreamCodec val$codec1, final StreamCodec val$codec2, final BiFunction val$constructor, final Function val$getter1, final Function val$getter2
   ) {
      this.val$codec1 = val$codec1x;
      this.val$codec2 = val$codec2x;
      this.val$constructor = val$constructorx;
      this.val$getter1 = val$getter1x;
      this.val$getter2 = val$getter2x;
   }

   @Override
   public C decode(final B input) {
      T1 v1 = (T1)this.val$codec1.decode(input);
      T2 v2 = (T2)this.val$codec2.decode(input);
      return (C)this.val$constructor.apply(v1, v2);
   }

   @Override
   public void encode(final B output, final C value) {
      this.val$codec1.encode(output, this.val$getter1.apply(value));
      this.val$codec2.encode(output, this.val$getter2.apply(value));
   }
}
