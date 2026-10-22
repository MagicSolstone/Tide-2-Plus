package net.minecraft.network.codec;

import com.mojang.datafixers.util.Function7;
import java.util.function.Function;

class StreamCodec$13 implements StreamCodec<B, C> {
   StreamCodec$13(
      final StreamCodec val$codec1,
      final StreamCodec val$codec2,
      final StreamCodec val$codec3,
      final StreamCodec val$codec4,
      final StreamCodec val$codec5,
      final StreamCodec val$codec6,
      final StreamCodec val$codec7,
      final Function7 val$constructor,
      final Function val$getter1,
      final Function val$getter2,
      final Function val$getter3,
      final Function val$getter4,
      final Function val$getter5,
      final Function val$getter6,
      final Function val$getter7
   ) {
      this.val$codec1 = val$codec1x;
      this.val$codec2 = val$codec2x;
      this.val$codec3 = val$codec3x;
      this.val$codec4 = val$codec4x;
      this.val$codec5 = val$codec5x;
      this.val$codec6 = val$codec6x;
      this.val$codec7 = val$codec7x;
      this.val$constructor = val$constructorx;
      this.val$getter1 = val$getter1x;
      this.val$getter2 = val$getter2x;
      this.val$getter3 = val$getter3x;
      this.val$getter4 = val$getter4x;
      this.val$getter5 = val$getter5x;
      this.val$getter6 = val$getter6x;
      this.val$getter7 = val$getter7x;
   }

   @Override
   public C decode(final B input) {
      T1 v1 = (T1)this.val$codec1.decode(input);
      T2 v2 = (T2)this.val$codec2.decode(input);
      T3 v3 = (T3)this.val$codec3.decode(input);
      T4 v4 = (T4)this.val$codec4.decode(input);
      T5 v5 = (T5)this.val$codec5.decode(input);
      T6 v6 = (T6)this.val$codec6.decode(input);
      T7 v7 = (T7)this.val$codec7.decode(input);
      return (C)this.val$constructor.apply(v1, v2, v3, v4, v5, v6, v7);
   }

   @Override
   public void encode(final B output, final C value) {
      this.val$codec1.encode(output, this.val$getter1.apply(value));
      this.val$codec2.encode(output, this.val$getter2.apply(value));
      this.val$codec3.encode(output, this.val$getter3.apply(value));
      this.val$codec4.encode(output, this.val$getter4.apply(value));
      this.val$codec5.encode(output, this.val$getter5.apply(value));
      this.val$codec6.encode(output, this.val$getter6.apply(value));
      this.val$codec7.encode(output, this.val$getter7.apply(value));
   }
}
