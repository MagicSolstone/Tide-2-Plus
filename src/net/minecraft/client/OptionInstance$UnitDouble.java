package net.minecraft.client;

import com.mojang.serialization.Codec;
import java.util.Optional;
import java.util.function.DoubleFunction;
import java.util.function.ToDoubleFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum OptionInstance$UnitDouble implements OptionInstance$SliderableValueSet<Double> {
   INSTANCE;

   public Optional<Double> validateValue(final Double value) {
      return value >= 0.0 && value <= 1.0 ? Optional.of(value) : Optional.empty();
   }

   public double toSliderValue(final Double value) {
      return value;
   }

   public Double fromSliderValue(final double slider) {
      return slider;
   }

   public <R> OptionInstance$SliderableValueSet<R> xmap(final DoubleFunction<? extends R> to, final ToDoubleFunction<? super R> from) {
      return new OptionInstance$UnitDouble$1(this, from, to);
   }

   @Override
   public Codec<Double> codec() {
      return Codec.withAlternative(Codec.doubleRange(0.0, 1.0), Codec.BOOL, b -> b ? 1.0 : 0.0);
   }
}
