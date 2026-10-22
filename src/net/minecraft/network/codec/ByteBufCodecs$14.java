package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.Utf8String;

class ByteBufCodecs$14 implements StreamCodec<ByteBuf, String> {
   ByteBufCodecs$14(final int val$maxStringLength) {
      this.val$maxStringLength = val$maxStringLengthx;
   }

   public String decode(final ByteBuf input) {
      return Utf8String.read(input, this.val$maxStringLength);
   }

   public void encode(final ByteBuf output, final String value) {
      Utf8String.write(output, value, this.val$maxStringLength);
   }
}
