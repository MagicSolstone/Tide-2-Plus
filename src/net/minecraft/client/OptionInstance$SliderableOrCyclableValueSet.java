package net.minecraft.client;

import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractWidget;

@Environment(EnvType.CLIENT)
public interface OptionInstance$SliderableOrCyclableValueSet<T> extends OptionInstance$SliderableValueSet<T>, OptionInstance$CycleableValueSet<T> {
   boolean createCycleButton();

   @Override
   default Function<OptionInstance<T>, AbstractWidget> createButton(
      final OptionInstance$TooltipSupplier<T> tooltip,
      final Options options,
      final int x,
      final int y,
      final int width,
      final OptionInstance$ValueUpdateListener<? super T> onValueChanged
   ) {
      return this.createCycleButton()
         ? OptionInstance$CycleableValueSet.super.createButton(tooltip, options, x, y, width, onValueChanged)
         : OptionInstance$SliderableValueSet.super.createButton(tooltip, options, x, y, width, onValueChanged);
   }
}
