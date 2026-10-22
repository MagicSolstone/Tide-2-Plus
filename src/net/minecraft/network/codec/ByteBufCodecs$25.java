package net.minecraft.network.codec;

import java.util.Optional;

class ByteBufCodecs$25 implements StreamCodec<B, Optional<V>> {
   ByteBufCodecs$25(final StreamCodec val$original) {
      this.val$original = val$originalx;
   }

   public Optional<V> decode(final B input) {
      return (Optional<V>)(input.readBoolean() ? Optional.of(this.val$original.decode(input)) : Optional.empty());
   }

   public void encode(final B output, final Optional<V> value) {
      if (value.isPresent()) {
         output.writeBoolean(true);
         this.val$original.encode(output, value.get());
      } else {
         output.writeBoolean(false);
      }
   }
}
