package net.minecraft.client;

import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.CycleButton$ValueListSupplier;

@Environment(EnvType.CLIENT)
public interface OptionInstance$CycleableValueSet<T> extends OptionInstance$ValueSet<T> {
   CycleButton$ValueListSupplier<T> valueListSupplier();

   default OptionInstance$CycleableValueSet$ValueSetter<T> valueSetter() {
      return OptionInstance::set;
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
      return instance -> CycleButton.builder(instance.toString, instance::get)
         .withValues(this.valueListSupplier())
         .withTooltip(tooltip)
         .create(x, y, width, 20, instance.caption, (var4x, value) -> {
            this.valueSetter().set(instance, value);
            options.save();
            onValueChanged.valueChanged(value);
         });
   }
}
