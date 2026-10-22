package net.minecraft.network.codec;

class StreamCodec$1 implements StreamCodec<B, V> {
   StreamCodec$1(final StreamDecoder val$decoder, final StreamEncoder val$encoder) {
      this.val$decoder = val$decoderx;
      this.val$encoder = val$encoderx;
   }

   @Override
   public V decode(final B input) {
      return (V)this.val$decoder.decode(input);
   }

   @Override
   public void encode(final B output, final V value) {
      this.val$encoder.encode(output, value);
   }
}
