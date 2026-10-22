package net.minecraft.client;

import java.util.Optional;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public interface OptionInstance$IntRangeBase extends OptionInstance$SliderableValueSet<Integer> {
   int minInclusive();

   int maxInclusive();

   default Optional<Integer> next(final Integer current) {
      return Optional.of(current + 1);
   }

   default Optional<Integer> previous(final Integer current) {
      return Optional.of(current - 1);
   }

   default double toSliderValue(final Integer value) {
      if (value == this.minInclusive()) {
         return 0.0;
      } else {
         return value == this.maxInclusive() ? 1.0 : Mth.map(value.intValue() + 0.5, (double)this.minInclusive(), this.maxInclusive() + 1.0, 0.0, 1.0);
      }
   }

   default Integer fromSliderValue(double slider) {
      if (slider >= 1.0) {
         slider = 0.99999F;
      }

      return Mth.floor(Mth.map(slider, 0.0, 1.0, (double)this.minInclusive(), this.maxInclusive() + 1.0));
   }

   default <R> OptionInstance$SliderableValueSet<R> xmap(final IntFunction<? extends R> to, final ToIntFunction<? super R> from, final boolean discrete) {
      return new OptionInstance$IntRangeBase$1(this, from, to, discrete);
   }
}
