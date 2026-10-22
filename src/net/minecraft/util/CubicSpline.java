package net.minecraft.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public sealed interface CubicSpline<I> permits CubicSpline$Multipoint, CubicSpline$Constant {
   void forEachCoordinate(Consumer<I> consumer);

   <R extends BoundedFloatFunction<?>> CubicSpline<R> mapCoordinates(Function<I, R> mapper);

   Interval range();

   @VisibleForDebug
   String parityString();

   static <C, I extends BoundedFloatFunction<C>> float sample(final CubicSpline<I> spline, final C coordinate) {
      return switch (spline) {
         case CubicSpline$Multipoint<I> multipoint -> CubicSpline$Multipoint.sample(multipoint, coordinate);
         case CubicSpline$Constant<I> constant -> constant.value();
         default -> throw new MatchException(null, null);
      };
   }

   static <C, I extends BoundedFloatFunction<C>> BoundedFloatFunction<C> asSampler(final CubicSpline<I> spline) {
      return (BoundedFloatFunction<C>)(switch (spline) {
         case CubicSpline$Multipoint<I> multipoint -> new CubicSpline$1(multipoint);
         case CubicSpline$Constant<I> constant -> BoundedFloatFunction.constant(constant.value());
         default -> throw new MatchException(null, null);
      });
   }

   static <I extends BoundedFloatFunction<?>> Codec<CubicSpline<I>> codec(final Codec<I> coordinateCodec) {
      return Codec.recursive(
         "CubicSpline",
         subSplineCodec -> Codec.either(Codec.FLOAT, CubicSpline$Multipoint.codec(coordinateCodec, subSplineCodec))
            .xmap(e -> (CubicSpline)e.map(CubicSpline$Constant::new, m -> m), spline -> {
               Objects.requireNonNull(spline);
               CubicSpline selector1$temp = spline;

               while (true) {
                  Either var10000;
                  switch (selector1$temp) {
                     case CubicSpline$Constant(float var10):
                        float patt3$temp = var10;
                        if (false) {
                           int index$2 = 1;
                           continue;
                        }

                        var10000 = Either.left(patt3$temp);
                        break;
                     case CubicSpline$Multipoint<I> multipoint:
                        var10000 = Either.right(multipoint);
                        break;
                     default:
                        throw new MatchException(null, null);
                  }

                  return var10000;
               }
            })
      );
   }

   static <I> CubicSpline<I> constant(final float value) {
      return new CubicSpline$Constant<>(value);
   }

   static <I extends BoundedFloatFunction<?>> CubicSpline$Builder<I> builder(final I coordinate) {
      return new CubicSpline$Builder<>(coordinate);
   }

   static <I extends BoundedFloatFunction<?>> CubicSpline$Builder<I> builder(final I coordinate, final Float2FloatFunction valueTransformer) {
      return new CubicSpline$Builder<>(coordinate, valueTransformer);
   }
}
