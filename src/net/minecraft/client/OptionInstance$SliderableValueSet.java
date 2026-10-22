package net.minecraft.client;

import java.util.Optional;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractWidget;

@Environment(EnvType.CLIENT)
public interface OptionInstance$SliderableValueSet<T> extends OptionInstance$ValueSet<T> {
   double toSliderValue(final T value);

   default Optional<T> next(final T current) {
      return Optional.empty();
   }

   default Optional<T> previous(final T current) {
      return Optional.empty();
   }

   T fromSliderValue(final double slider);

   default boolean applyValueImmediately() {
      return true;
   }

   @Override
   default Function<OptionInstance<T>, AbstractWidget> createButton(
      final OptionInstance$TooltipSupplier<T> tooltip,
      final Options options,
      final int x,
      final int y,
      final int width,
      final OptionInstance$ValueUpdateListener<? super T> onValueChanged
   ) {
      return instance -> new OptionInstance$OptionInstanceSliderButton<>(
         options, x, y, width, 20, instance, this, tooltip, onValueChanged, this.applyValueImmediately()
      );
   }
}
