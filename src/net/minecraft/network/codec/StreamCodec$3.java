package net.minecraft.network.codec;

class StreamCodec$3 implements StreamCodec<B, V> {
   StreamCodec$3(final Object val$instance) {
      this.val$instance = val$instancex;
   }

   @Override
   public V decode(final B input) {
      return (V)this.val$instance;
   }

   @Override
   public void encode(final B output, final V value) {
      if (!value.equals(this.val$instance)) {
         throw new IllegalStateException("Can't encode '" + value + "', expected '" + this.val$instance + "'");
      }
   }
}
