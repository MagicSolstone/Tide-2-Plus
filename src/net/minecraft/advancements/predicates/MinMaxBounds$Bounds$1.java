package net.minecraft.advancements.predicates;

import java.util.Optional;
import net.minecraft.network.codec.StreamCodec;

class MinMaxBounds$Bounds$1 implements StreamCodec<B, MinMaxBounds$Bounds<T>> {
   private static final int MIN_FLAG = 1;
   private static final int MAX_FLAG = 2;

   MinMaxBounds$Bounds$1(final StreamCodec val$numberCodec) {
      this.val$numberCodec = val$numberCodecx;
   }

   public MinMaxBounds$Bounds<T> decode(final B input) {
      byte flags = input.readByte();
      Optional<T> min = (Optional<T>)((flags & 1) != 0 ? Optional.of((Number)this.val$numberCodec.decode(input)) : Optional.empty());
      Optional<T> max = (Optional<T>)((flags & 2) != 0 ? Optional.of((Number)this.val$numberCodec.decode(input)) : Optional.empty());
      return new MinMaxBounds$Bounds(min, max);
   }

   public void encode(final B output, final MinMaxBounds$Bounds<T> value) {
      Optional<T> min = value.min();
      Optional<T> max = value.max();
      output.writeByte((min.isPresent() ? 1 : 0) | (max.isPresent() ? 2 : 0));
      min.ifPresent(v -> numberCodec.encode(output, v));
      max.ifPresent(v -> numberCodec.encode(output, v));
   }
}
