package net.minecraft.client.gui.screens.dialog.input;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.dialog.input.NumberRangeInput;

@Environment(EnvType.CLIENT)
class InputControlHandlers$NumberRangeHandler implements InputControlHandler<NumberRangeInput> {
   private InputControlHandlers$NumberRangeHandler() {
   }

   public void addControl(final NumberRangeInput input, final Screen screen, final InputControlHandler$Output output) {
      float initialValue = input.rangeInfo().initialSliderValue();
      InputControlHandlers$NumberRangeHandler$SliderImpl control = new InputControlHandlers$NumberRangeHandler$SliderImpl(input, (double)initialValue);
      output.accept(control, new InputControlHandlers$NumberRangeHandler$1(this, control));
   }
}
