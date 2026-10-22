package net.minecraft.network.codec;

import com.mojang.datafixers.util.Function3;
import java.util.function.Function;

class StreamCodec$9 implements StreamCodec<B, C> {
   StreamCodec$9(
      final StreamCodec val$codec1,
      final StreamCodec val$codec2,
      final StreamCodec val$codec3,
      final Function3 val$constructor,
      final Function val$getter1,
      final Function val$getter2,
      final Function val$getter3
   ) {
      this.val$codec1 = val$codec1x;
      this.val$codec2 = val$codec2x;
      this.val$codec3 = val$codec3x;
      this.val$constructor = val$constructorx;
      this.val$getter1 = val$getter1x;
      this.val$getter2 = val$getter2x;
      this.val$getter3 = val$getter3x;
   }

   @Override
   public C decode(final B input) {
      T1 v1 = (T1)this.val$codec1.decode(input);
      T2 v2 = (T2)this.val$codec2.decode(input);
      T3 v3 = (T3)this.val$codec3.decode(input);
      return (C)this.val$constructor.apply(v1, v2, v3);
   }

   @Override
   public void encode(final B output, final C value) {
      this.val$codec1.encode(output, this.val$getter1.apply(value));
      this.val$codec2.encode(output, this.val$getter2.apply(value));
      this.val$codec3.encode(output, this.val$getter3.apply(value));
   }
}
