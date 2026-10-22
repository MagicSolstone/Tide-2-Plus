package net.minecraft.network.codec;

import com.mojang.datafixers.util.Either;

class ByteBufCodecs$29 implements StreamCodec<B, Either<L, R>> {
   ByteBufCodecs$29(final StreamCodec val$leftCodec, final StreamCodec val$rightCodec) {
      this.val$leftCodec = val$leftCodecx;
      this.val$rightCodec = val$rightCodecx;
   }

   public Either<L, R> decode(final B input) {
      return input.readBoolean() ? Either.left(this.val$leftCodec.decode(input)) : Either.right(this.val$rightCodec.decode(input));
   }

   public void encode(final B output, final Either<L, R> value) {
      value.ifLeft(left -> {
         output.writeBoolean(true);
         leftCodec.encode(output, left);
      }).ifRight(right -> {
         output.writeBoolean(false);
         rightCodec.encode(output, right);
      });
   }
}
