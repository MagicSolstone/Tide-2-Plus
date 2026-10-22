package net.minecraft.network.codec;

import com.mojang.datafixers.util.Function14;
import java.util.function.Function;

class StreamCodec$20 implements StreamCodec<B, C> {
   StreamCodec$20(
      final StreamCodec val$codec1,
      final StreamCodec val$codec2,
      final StreamCodec val$codec3,
      final StreamCodec val$codec4,
      final StreamCodec val$codec5,
      final StreamCodec val$codec6,
      final StreamCodec val$codec7,
      final StreamCodec val$codec8,
      final StreamCodec val$codec9,
      final StreamCodec val$codec10,
      final StreamCodec val$codec11,
      final StreamCodec val$codec12,
      final StreamCodec val$codec13,
      final StreamCodec val$codec14,
      final Function14 val$constructor,
      final Function val$getter1,
      final Function val$getter2,
      final Function val$getter3,
      final Function val$getter4,
      final Function val$getter5,
      final Function val$getter6,
      final Function val$getter7,
      final Function val$getter8,
      final Function val$getter9,
      final Function val$getter10,
      final Function val$getter11,
      final Function val$getter12,
      final Function val$getter13,
      final Function val$getter14
   ) {
      this.val$codec1 = val$codec1x;
      this.val$codec2 = val$codec2x;
      this.val$codec3 = val$codec3x;
      this.val$codec4 = val$codec4x;
      this.val$codec5 = val$codec5x;
      this.val$codec6 = val$codec6x;
      this.val$codec7 = val$codec7x;
      this.val$codec8 = val$codec8x;
      this.val$codec9 = val$codec9x;
      this.val$codec10 = val$codec10x;
      this.val$codec11 = val$codec11x;
      this.val$codec12 = val$codec12x;
      this.val$codec13 = val$codec13x;
      this.val$codec14 = val$codec14x;
      this.val$constructor = val$constructorx;
      this.val$getter1 = val$getter1x;
      this.val$getter2 = val$getter2x;
      this.val$getter3 = val$getter3x;
      this.val$getter4 = val$getter4x;
      this.val$getter5 = val$getter5x;
      this.val$getter6 = val$getter6x;
      this.val$getter7 = val$getter7x;
      this.val$getter8 = val$getter8x;
      this.val$getter9 = val$getter9x;
      this.val$getter10 = val$getter10x;
      this.val$getter11 = val$getter11x;
      this.val$getter12 = val$getter12x;
      this.val$getter13 = val$getter13x;
      this.val$getter14 = val$getter14x;
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
      T8 v8 = (T8)this.val$codec8.decode(input);
      T9 v9 = (T9)this.val$codec9.decode(input);
      T10 v10 = (T10)this.val$codec10.decode(input);
      T11 v11 = (T11)this.val$codec11.decode(input);
      T12 v12 = (T12)this.val$codec12.decode(input);
      T13 v13 = (T13)this.val$codec13.decode(input);
      T14 v14 = (T14)this.val$codec14.decode(input);
      return (C)this.val$constructor.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14);
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
      this.val$codec8.encode(output, this.val$getter8.apply(value));
      this.val$codec9.encode(output, this.val$getter9.apply(value));
      this.val$codec10.encode(output, this.val$getter10.apply(value));
      this.val$codec11.encode(output, this.val$getter11.apply(value));
      this.val$codec12.encode(output, this.val$getter12.apply(value));
      this.val$codec13.encode(output, this.val$getter13.apply(value));
      this.val$codec14.encode(output, this.val$getter14.apply(value));
   }
}
