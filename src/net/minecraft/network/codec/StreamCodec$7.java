package net.minecraft.network.codec;

import java.util.function.Function;

class StreamCodec$7 implements StreamCodec<B, C> {
   StreamCodec$7(final StreamCodec val$codec1, final Function val$constructor, final Function val$getter1) {
      this.val$codec1 = val$codec1x;
      this.val$constructor = val$constructorx;
      this.val$getter1 = val$getter1x;
   }

   @Override
   public C decode(final B input) {
      T1 v1 = (T1)this.val$codec1.decode(input);
      return (C)this.val$constructor.apply(v1);
   }

   @Override
   public void encode(final B output, final C value) {
      this.val$codec1.encode(output, this.val$getter1.apply(value));
   }
}
