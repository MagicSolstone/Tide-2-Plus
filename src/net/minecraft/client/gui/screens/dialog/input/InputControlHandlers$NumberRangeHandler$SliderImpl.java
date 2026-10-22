package net.minecraft.client.gui.screens.dialog.input;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.server.dialog.input.NumberRangeInput;

@Environment(EnvType.CLIENT)
class InputControlHandlers$NumberRangeHandler$SliderImpl extends AbstractSliderButton {
   private final NumberRangeInput input;

   private InputControlHandlers$NumberRangeHandler$SliderImpl(final NumberRangeInput input, final double initialSliderValue) {
      super(0, 0, input.width(), 20, computeMessage(input, initialSliderValue), initialSliderValue);
      this.input = input;
   }

   @Override
   protected void updateMessage() {
      this.setMessage(computeMessage(this.input, this.value));
   }

   @Override
   protected void applyValue() {
   }

   public String stringValueToSend() {
      return sliderValueToString(this.input, this.value);
   }

   public float floatValueToSend() {
      return scaledValue(this.input, this.value);
   }

   private static float scaledValue(final NumberRangeInput input, final double sliderValue) {
      return input.rangeInfo().computeScaledValue((float)sliderValue);
   }

   private static String sliderValueToString(final NumberRangeInput input, final double sliderValue) {
      return valueToString(scaledValue(input, sliderValue));
   }

   private static Component computeMessage(final NumberRangeInput input, final double sliderValue) {
      return input.computeLabel(sliderValueToString(input, sliderValue));
   }

   private static String valueToString(final float v) {
      int intV = (int)v;
      return intV == v ? Integer.toString(intV) : Float.toString(v);
   }
}
