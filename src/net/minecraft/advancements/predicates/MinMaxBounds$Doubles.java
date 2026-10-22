package net.minecraft.advancements.predicates;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;

public record MinMaxBounds$Doubles(MinMaxBounds$Bounds<Double> bounds, MinMaxBounds$Bounds<Double> boundsSqr) implements MinMaxBounds<Double> {
   public static final MinMaxBounds$Doubles ANY = new MinMaxBounds$Doubles(MinMaxBounds$Bounds.any());
   public static final Codec<MinMaxBounds$Doubles> CODEC = MinMaxBounds$Bounds.createCodec(Codec.DOUBLE)
      .validate(MinMaxBounds$Bounds::validateSwappedBoundsInCodec)
      .xmap(MinMaxBounds$Doubles::new, MinMaxBounds$Doubles::bounds);
   public static final StreamCodec<ByteBuf, MinMaxBounds$Doubles> STREAM_CODEC = MinMaxBounds$Bounds.createStreamCodec(ByteBufCodecs.DOUBLE)
      .map(MinMaxBounds$Doubles::new, MinMaxBounds$Doubles::bounds);

   private MinMaxBounds$Doubles(final MinMaxBounds$Bounds<Double> bounds) {
      this(bounds, bounds.map(Mth::square));
   }

   public static MinMaxBounds$Doubles exactly(final double value) {
      return new MinMaxBounds$Doubles(MinMaxBounds$Bounds.exactly(value));
   }

   public static MinMaxBounds$Doubles between(final double min, final double max) {
      return new MinMaxBounds$Doubles(MinMaxBounds$Bounds.between(min, max));
   }

   public static MinMaxBounds$Doubles atLeast(final double value) {
      return new MinMaxBounds$Doubles(MinMaxBounds$Bounds.atLeast(value));
   }

   public static MinMaxBounds$Doubles atMost(final double value) {
      return new MinMaxBounds$Doubles(MinMaxBounds$Bounds.atMost(value));
   }

   public boolean matches(final double value) {
      return this.bounds.min.isPresent() && this.bounds.min.get() > value ? false : this.bounds.max.isEmpty() || !(this.bounds.max.get() < value);
   }

   public boolean matchesSqr(final double valueSqr) {
      return this.boundsSqr.min.isPresent() && this.boundsSqr.min.get() > valueSqr
         ? false
         : this.boundsSqr.max.isEmpty() || !(this.boundsSqr.max.get() < valueSqr);
   }

   public static MinMaxBounds$Doubles fromReader(final StringReader reader) throws CommandSyntaxException {
      int start = reader.getCursor();
      MinMaxBounds$Bounds<Double> bounds = MinMaxBounds$Bounds.fromReader(
         reader, Double::parseDouble, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidDouble
      );
      if (bounds.areSwapped()) {
         reader.setCursor(start);
         throw ERROR_SWAPPED.createWithContext(reader);
      } else {
         return new MinMaxBounds$Doubles(bounds);
      }
   }
}
