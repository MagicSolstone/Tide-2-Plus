package net.minecraft.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

record CubicSpline$Multipoint$Point<I extends BoundedFloatFunction<?>>(float location, CubicSpline<I> value, float derivative) {
   public static <I extends BoundedFloatFunction<?>> Codec<CubicSpline$Multipoint$Point<I>> codec(final Codec<CubicSpline<I>> subSplineCodec) {
      return RecordCodecBuilder.create(
         i -> i.group(
               Codec.FLOAT.fieldOf("location").forGetter(CubicSpline$Multipoint$Point::location),
               subSplineCodec.fieldOf("value").forGetter(CubicSpline$Multipoint$Point::value),
               Codec.FLOAT.fieldOf("derivative").forGetter(CubicSpline$Multipoint$Point::derivative)
            )
            .apply(i, CubicSpline$Multipoint$Point::new)
      );
   }
}
