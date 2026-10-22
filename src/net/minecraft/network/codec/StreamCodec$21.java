package net.minecraft.network.codec;

import com.google.common.base.Suppliers;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

class StreamCodec$21 implements StreamCodec<B, T> {
   private final Supplier<StreamCodec<B, T>> inner;

   StreamCodec$21(final UnaryOperator val$factory) {
      this.val$factory = val$factoryx;
      this.inner = Suppliers.memoize(() -> factory.apply(this));
   }

   @Override
   public T decode(final B input) {
      return (T)((StreamCodec)this.inner.get()).decode(input);
   }

   @Override
   public void encode(final B output, final T value) {
      ((StreamCodec)this.inner.get()).encode(output, value);
   }
}
