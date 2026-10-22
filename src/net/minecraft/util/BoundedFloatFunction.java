package net.minecraft.util;

import java.util.function.Function;

public interface BoundedFloatFunction<C> {
   BoundedFloatFunction<Float> IDENTITY = new BoundedFloatFunction$2();

   float apply(final C c);

   Interval range();

   static <C> BoundedFloatFunction<C> constant(final float value) {
      Interval range = Interval.ofExact(value);
      return new BoundedFloatFunction$1(value, range);
   }

   default <C2> BoundedFloatFunction<C2> comap(final Function<C2, C> function) {
      return new BoundedFloatFunction$3(this, this, function);
   }
}
