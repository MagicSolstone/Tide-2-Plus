package net.minecraft.network.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;

class ByteBufCodecs$19 implements StreamCodec<B, V> {
   ByteBufCodecs$19(final StreamCodec val$original, final Codec val$codec, final DynamicOps val$ops) {
      this.val$original = val$originalx;
      this.val$codec = val$codecx;
      this.val$ops = val$opsx;
   }

   public V decode(final B input) {
      T payload = (T)this.val$original.decode(input);
      return (V)this.val$codec.parse(this.val$ops, payload).getOrThrow(msg -> new DecoderException("Failed to decode: " + msg + " " + payload));
   }

   public void encode(final B output, final V value) {
      T payload = (T)this.val$codec.encodeStart(this.val$ops, value).getOrThrow(msg -> new EncoderException("Failed to encode: " + msg + " " + value));
      this.val$original.encode(output, payload);
   }
}
