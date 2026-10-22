package net.minecraft.network.codec;

class StreamCodec$2 implements StreamCodec<B, V> {
   StreamCodec$2(final StreamDecoder val$decoder, final StreamMemberEncoder val$encoder) {
      this.val$decoder = val$decoderx;
      this.val$encoder = val$encoderx;
   }

   @Override
   public V decode(final B input) {
      return (V)this.val$decoder.decode(input);
   }

   @Override
   public void encode(final B output, final V value) {
      this.val$encoder.encode(value, output);
   }
}
