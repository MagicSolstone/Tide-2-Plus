package net.minecraft.advancements.predicates;

import com.google.common.collect.Range;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.network.codec.StreamCodec;

public record MinMaxBounds$Bounds<T extends Number & Comparable<T>>(Optional<T> min, Optional<T> max) {
   public boolean isAny() {
      return this.min().isEmpty() && this.max().isEmpty();
   }

   public DataResult<MinMaxBounds$Bounds<T>> validateSwappedBoundsInCodec() {
      return this.areSwapped() ? DataResult.error(() -> "Swapped bounds in range: " + this.min() + " is higher than " + this.max()) : DataResult.success(this);
   }

   public boolean areSwapped() {
      return this.min.isPresent() && this.max.isPresent() && this.min.get().compareTo(this.max.get()) > 0;
   }

   public Range<T> asRange() {
      if (this.min.isPresent()) {
         return this.max.isPresent() ? Range.closed(this.min.get(), this.max.get()) : Range.atLeast(this.min.get());
      } else {
         return this.max.isPresent() ? Range.atMost(this.max.get()) : Range.all();
      }
   }

   public Optional<T> asPoint() {
      Optional<T> min = this.min();
      Optional<T> max = this.max();
      return min.equals(max) ? min : Optional.empty();
   }

   public static <T extends Number & Comparable<T>> MinMaxBounds$Bounds<T> any() {
      return new MinMaxBounds$Bounds<>(Optional.empty(), Optional.empty());
   }

   public static <T extends Number & Comparable<T>> MinMaxBounds$Bounds<T> exactly(final T value) {
      Optional<T> wrapped = Optional.of(value);
      return new MinMaxBounds$Bounds<>(wrapped, wrapped);
   }

   public static <T extends Number & Comparable<T>> MinMaxBounds$Bounds<T> between(final T min, final T max) {
      return new MinMaxBounds$Bounds<>(Optional.of(min), Optional.of(max));
   }

   public static <T extends Number & Comparable<T>> MinMaxBounds$Bounds<T> atLeast(final T value) {
      return new MinMaxBounds$Bounds<>(Optional.of(value), Optional.empty());
   }

   public static <T extends Number & Comparable<T>> MinMaxBounds$Bounds<T> atMost(final T value) {
      return new MinMaxBounds$Bounds<>(Optional.empty(), Optional.of(value));
   }

   public <U extends Number & Comparable<U>> MinMaxBounds$Bounds<U> map(final Function<T, U> mapper) {
      return new MinMaxBounds$Bounds<>(this.min.map(mapper), this.max.map(mapper));
   }

   public static <T extends Number & Comparable<T>> Codec<MinMaxBounds$Bounds<T>> createCodec(final Codec<T> numberCodec) {
      Codec<MinMaxBounds$Bounds<T>> rangeCodec = RecordCodecBuilder.create(
         i -> i.group(
               numberCodec.optionalFieldOf("min").forGetter(MinMaxBounds$Bounds::min), numberCodec.optionalFieldOf("max").forGetter(MinMaxBounds$Bounds::max)
            )
            .apply(i, MinMaxBounds$Bounds::new)
      );
      return Codec.either(rangeCodec, numberCodec).xmap(either -> (MinMaxBounds$Bounds)either.map(v -> v, x$0 -> exactly((T)x$0)), bounds -> {
         Optional<T> point = bounds.asPoint();
         return point.isPresent() ? Either.right(point.get()) : Either.left(bounds);
      });
   }

   public static <B extends ByteBuf, T extends Number & Comparable<T>> StreamCodec<B, MinMaxBounds$Bounds<T>> createStreamCodec(
      final StreamCodec<B, T> numberCodec
   ) {
      return new MinMaxBounds$Bounds$1(numberCodec);
   }

   public static <T extends Number & Comparable<T>> MinMaxBounds$Bounds<T> fromReader(
      final StringReader reader, final Function<String, T> converter, final Supplier<DynamicCommandExceptionType> parseExc
   ) throws CommandSyntaxException {
      if (!reader.canRead()) {
         throw MinMaxBounds.ERROR_EMPTY.createWithContext(reader);
      } else {
         int start = reader.getCursor();

         try {
            Optional<T> min = readNumber(reader, converter, parseExc);
            Optional<T> max;
            if (reader.canRead(2) && reader.peek() == '.' && reader.peek(1) == '.') {
               reader.skip();
               reader.skip();
               max = readNumber(reader, converter, parseExc);
            } else {
               max = min;
            }

            if (min.isEmpty() && max.isEmpty()) {
               throw MinMaxBounds.ERROR_EMPTY.createWithContext(reader);
            } else {
               return new MinMaxBounds$Bounds<>(min, max);
            }
         } catch (CommandSyntaxException var6) {
            reader.setCursor(start);
            throw new CommandSyntaxException(var6.getType(), var6.getRawMessage(), var6.getInput(), start);
         }
      }
   }

   private static <T extends Number> Optional<T> readNumber(
      final StringReader reader, final Function<String, T> converter, final Supplier<DynamicCommandExceptionType> parseExc
   ) throws CommandSyntaxException {
      int start = reader.getCursor();

      while (reader.canRead() && isAllowedInputChar(reader)) {
         reader.skip();
      }

      String number = reader.getString().substring(start, reader.getCursor());
      if (number.isEmpty()) {
         return Optional.empty();
      } else {
         try {
            return Optional.of(converter.apply(number));
         } catch (NumberFormatException var6) {
            throw parseExc.get().createWithContext(reader, number);
         }
      }
   }

   private static boolean isAllowedInputChar(final StringReader reader) {
      char c = reader.peek();
      if ((c < '0' || c > '9') && c != '-') {
         return c != '.' ? false : !reader.canRead(2) || reader.peek(1) != '.';
      } else {
         return true;
      }
   }
}
