package net.minecraft.network.codec;

import java.util.Objects;
import java.util.function.Function;

class StreamCodec$4 implements StreamCodec<B, O> {
   StreamCodec$4(final StreamCodec this$0, final Function val$to, final Function val$from) {
      this.val$to = val$tox;
      this.val$from = val$fromx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public O decode(final B input) {
      return (O)this.val$to.apply(this.this$0.decode(input));
   }

   @Override
   public void encode(final B output, final O value) {
      this.this$0.encode(output, this.val$from.apply(value));
   }
}
