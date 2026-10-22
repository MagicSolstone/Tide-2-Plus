package net.minecraft.advancements.predicates;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;

public record MinMaxBounds$Ints(MinMaxBounds$Bounds<Integer> bounds, MinMaxBounds$Bounds<Long> boundsSqr) implements MinMaxBounds<Integer> {
   public static final MinMaxBounds$Ints ANY = new MinMaxBounds$Ints(MinMaxBounds$Bounds.any());
   public static final Codec<MinMaxBounds$Ints> CODEC = MinMaxBounds$Bounds.createCodec(Codec.INT)
      .validate(MinMaxBounds$Bounds::validateSwappedBoundsInCodec)
      .xmap(MinMaxBounds$Ints::new, MinMaxBounds$Ints::bounds);
   public static final StreamCodec<ByteBuf, MinMaxBounds$Ints> STREAM_CODEC = MinMaxBounds$Bounds.createStreamCodec(ByteBufCodecs.INT)
      .map(MinMaxBounds$Ints::new, MinMaxBounds$Ints::bounds);

   private MinMaxBounds$Ints(final MinMaxBounds$Bounds<Integer> bounds) {
      this(bounds, bounds.map(i -> Mth.square(i.longValue())));
   }

   public static MinMaxBounds$Ints exactly(final int value) {
      return new MinMaxBounds$Ints(MinMaxBounds$Bounds.exactly(value));
   }

   public static MinMaxBounds$Ints between(final int min, final int max) {
      return new MinMaxBounds$Ints(MinMaxBounds$Bounds.between(min, max));
   }

   public static MinMaxBounds$Ints atLeast(final int value) {
      return new MinMaxBounds$Ints(MinMaxBounds$Bounds.atLeast(value));
   }

   public static MinMaxBounds$Ints atMost(final int value) {
      return new MinMaxBounds$Ints(MinMaxBounds$Bounds.atMost(value));
   }

   public boolean matches(final int value) {
      return this.bounds.min.isPresent() && this.bounds.min.get() > value ? false : this.bounds.max.isEmpty() || this.bounds.max.get() >= value;
   }

   public boolean matchesSqr(final long valueSqr) {
      return this.boundsSqr.min.isPresent() && this.boundsSqr.min.get() > valueSqr
         ? false
         : this.boundsSqr.max.isEmpty() || this.boundsSqr.max.get() >= valueSqr;
   }

   public static MinMaxBounds$Ints fromReader(final StringReader reader) throws CommandSyntaxException {
      int start = reader.getCursor();
      MinMaxBounds$Bounds<Integer> bounds = MinMaxBounds$Bounds.fromReader(
         reader, Integer::parseInt, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidInt
      );
      if (bounds.areSwapped()) {
         reader.setCursor(start);
         throw ERROR_SWAPPED.createWithContext(reader);
      } else {
         return new MinMaxBounds$Ints(bounds);
      }
   }
}
