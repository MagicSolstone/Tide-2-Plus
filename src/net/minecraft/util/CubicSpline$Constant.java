package net.minecraft.util;

import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;

@VisibleForDebug
public record CubicSpline$Constant<I>(float value) implements CubicSpline<I> {
   @Override
   public String parityString() {
      return String.format(Locale.ROOT, "k=%.3f", this.value);
   }

   @Override
   public Interval range() {
      return Interval.ofExact(this.value);
   }

   @Override
   public void forEachCoordinate(final Consumer<I> consumer) {
   }

   @Override
   public <R extends BoundedFloatFunction<?>> CubicSpline<R> mapCoordinates(final Function<I, R> mapper) {
      return this;
   }
}
