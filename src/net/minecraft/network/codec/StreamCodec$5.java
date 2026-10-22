package net.minecraft.network.codec;

import java.util.Objects;
import java.util.function.Function;

class StreamCodec$5 implements StreamCodec<O, V> {
   StreamCodec$5(final StreamCodec this$0, final Function val$operation) {
      this.val$operation = val$operationx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   public V decode(final O input) {
      B wrappedStream = (B)this.val$operation.apply(input);
      return (V)this.this$0.decode(wrappedStream);
   }

   public void encode(final O output, final V value) {
      B wrappedStream = (B)this.val$operation.apply(output);
      this.this$0.encode(wrappedStream, value);
   }
}
