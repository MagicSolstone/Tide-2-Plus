package net.minecraft.client;

import java.util.Objects;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractOptionSliderButton;
import net.minecraft.client.gui.components.ResettableOptionWidget;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class OptionInstance$OptionInstanceSliderButton<N> extends AbstractOptionSliderButton implements ResettableOptionWidget {
   private final OptionInstance<N> instance;
   private final OptionInstance$SliderableValueSet<N> values;
   private final OptionInstance$TooltipSupplier<N> tooltipSupplier;
   private final OptionInstance$ValueUpdateListener<? super N> onValueChanged;
   @Nullable
   private Long delayedApplyAt;
   private final boolean applyValueImmediately;

   private OptionInstance$OptionInstanceSliderButton(
      final Options options,
      final int x,
      final int y,
      final int width,
      final int height,
      final OptionInstance<N> instance,
      final OptionInstance$SliderableValueSet<N> values,
      final OptionInstance$TooltipSupplier<N> tooltipSupplier,
      final OptionInstance$ValueUpdateListener<? super N> onValueChanged,
      final boolean applyValueImmediately
   ) {
      super(options, x, y, width, height, values.toSliderValue(instance.get()));
      this.instance = instance;
      this.values = values;
      this.tooltipSupplier = tooltipSupplier;
      this.onValueChanged = onValueChanged;
      this.applyValueImmediately = applyValueImmediately;
      this.updateMessage();
   }

   @Override
   protected void updateMessage() {
      this.setMessage(this.instance.toString.apply(this.values.fromSliderValue(this.value)));
      this.setTooltip(this.tooltipSupplier.apply(this.values.fromSliderValue(this.value)));
   }

   @Override
   protected void applyValue() {
      if (this.applyValueImmediately) {
         this.applyUnsavedValue();
      } else {
         this.delayedApplyAt = Util.getMillis() + 600L;
      }
   }

   public void applyUnsavedValue() {
      N sliderValue = this.values.fromSliderValue(this.value);
      if (!Objects.equals(sliderValue, this.instance.get())) {
         this.instance.set(sliderValue);
         this.onValueChanged.valueChanged(this.instance.get());
      }
   }

   @Override
   public void resetValue() {
      if (this.value != this.values.toSliderValue(this.instance.get())) {
         this.value = this.values.toSliderValue(this.instance.get());
         this.delayedApplyAt = null;
         this.updateMessage();
      }
   }

   @Override
   public void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
      if (this.delayedApplyAt != null && Util.getMillis() >= this.delayedApplyAt) {
         this.delayedApplyAt = null;
         this.applyUnsavedValue();
         this.resetValue();
      }
   }

   @Override
   public void onRelease(final MouseButtonEvent event) {
      super.onRelease(event);
      if (this.applyValueImmediately) {
         this.resetValue();
      }
   }

   @Override
   public boolean keyPressed(final KeyEvent event) {
      if (event.isSelection()) {
         this.canChangeValue = !this.canChangeValue;
         return true;
      } else {
         if (this.canChangeValue) {
            boolean left = event.isLeft();
            boolean right = event.isRight();
            if (left) {
               Optional<N> previous = this.values.previous(this.values.fromSliderValue(this.value));
               if (previous.isPresent()) {
                  this.setValue(this.values.toSliderValue(previous.get()));
                  return true;
               }
            }

            if (right) {
               Optional<N> next = this.values.next(this.values.fromSliderValue(this.value));
               if (next.isPresent()) {
                  this.setValue(this.values.toSliderValue(next.get()));
                  return true;
               }
            }

            if (left || right) {
               float direction = left ? -1.0F : 1.0F;
               this.setValue(this.value + direction / (this.width - 8));
               return true;
            }
         }

         return false;
      }
   }
}
