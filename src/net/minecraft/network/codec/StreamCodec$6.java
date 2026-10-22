package net.minecraft.network.codec;

import java.util.Objects;
import java.util.function.Function;

class StreamCodec$6 implements StreamCodec<B, U> {
   StreamCodec$6(final StreamCodec this$0, final Function val$codec, final Function val$type) {
      this.val$codec = val$codecx;
      this.val$type = val$typex;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public U decode(final B input) {
      V key = (V)this.this$0.decode(input);
      StreamCodec<? super B, ? extends U> valueCodec = (StreamCodec<? super B, ? extends U>)this.val$codec.apply(key);
      return (U)valueCodec.decode(input);
   }

   @Override
   public void encode(final B output, final U value) {
      V key = (V)this.val$type.apply(value);
      StreamCodec<B, U> valueCodec = (StreamCodec<B, U>)this.val$codec.apply(key);
      this.this$0.encode(output, key);
      valueCodec.encode(output, value);
   }
}
