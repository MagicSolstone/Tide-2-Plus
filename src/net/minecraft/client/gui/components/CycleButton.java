package net.minecraft.client.gui.components;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance$TooltipSupplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.GuiGraphicsExtractor$HoveredTextEffects;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class CycleButton<T> extends AbstractButton implements ResettableOptionWidget {
   public static final BooleanSupplier DEFAULT_ALT_LIST_SELECTOR = () -> Minecraft.getInstance().hasAltDown();
   private static final List<Boolean> BOOLEAN_OPTIONS = ImmutableList.of(Boolean.TRUE, Boolean.FALSE);
   private final Supplier<T> defaultValueSupplier;
   private final Component name;
   private int index;
   private T value;
   private final CycleButton$ValueListSupplier<T> values;
   private final Function<T, Component> valueStringifier;
   private final Function<CycleButton<T>, MutableComponent> narrationProvider;
   private final CycleButton$OnValueChange<T> onValueChange;
   private final CycleButton$DisplayState displayState;
   private final OptionInstance$TooltipSupplier<T> tooltipSupplier;
   private final CycleButton$SpriteSupplier<T> spriteSupplier;

   private CycleButton(
      final int x,
      final int y,
      final int width,
      final int height,
      final Component message,
      final Component name,
      final int index,
      final T value,
      final Supplier<T> defaultValueSupplier,
      final CycleButton$ValueListSupplier<T> values,
      final Function<T, Component> valueStringifier,
      final Function<CycleButton<T>, MutableComponent> narrationProvider,
      final CycleButton$OnValueChange<T> onValueChange,
      final OptionInstance$TooltipSupplier<T> tooltipSupplier,
      final CycleButton$DisplayState displayState,
      final CycleButton$SpriteSupplier<T> spriteSupplier
   ) {
      super(x, y, width, height, message);
      this.name = name;
      this.index = index;
      this.defaultValueSupplier = defaultValueSupplier;
      this.value = value;
      this.values = values;
      this.valueStringifier = valueStringifier;
      this.narrationProvider = narrationProvider;
      this.onValueChange = onValueChange;
      this.displayState = displayState;
      this.tooltipSupplier = tooltipSupplier;
      this.spriteSupplier = spriteSupplier;
      this.updateTooltip();
   }

   @Override
   protected void extractContents(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      Identifier sprite = this.spriteSupplier.apply(this, this.getValue());
      if (sprite != null) {
         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, this.getX(), this.getY(), this.getWidth(), this.getHeight());
      } else {
         this.extractDefaultSprite(graphics);
      }

      if (this.displayState != CycleButton$DisplayState.HIDE) {
         this.extractDefaultLabel(graphics.textRendererForWidget(this, GuiGraphicsExtractor$HoveredTextEffects.NONE));
      }
   }

   private void updateTooltip() {
      this.setTooltip(this.tooltipSupplier.apply(this.value));
   }

   @Override
   public void onPress(final InputWithModifiers input) {
      if (input.hasShiftDown()) {
         this.cycleValue(-1);
      } else {
         this.cycleValue(1);
      }
   }

   private void cycleValue(final int delta) {
      List<T> list = this.values.getSelectedList();
      this.index = Mth.positiveModulo(this.index + delta, list.size());
      T newValue = list.get(this.index);
      this.updateValue(newValue);
      this.onValueChange.onValueChange(this, newValue);
   }

   private T getCycledValue(final int delta) {
      List<T> list = this.values.getSelectedList();
      return list.get(Mth.positiveModulo(this.index + delta, list.size()));
   }

   @Override
   public boolean mouseScrolled(final double x, final double y, final double scrollX, final double scrollY) {
      if (scrollY > 0.0) {
         this.cycleValue(-1);
      } else if (scrollY < 0.0) {
         this.cycleValue(1);
      }

      return true;
   }

   public void setValue(final T newValue) {
      List<T> list = this.values.getSelectedList();
      int newIndex = list.indexOf(newValue);
      if (newIndex != -1) {
         this.index = newIndex;
      }

      this.updateValue(newValue);
   }

   @Override
   public void resetValue() {
      this.setValue(this.defaultValueSupplier.get());
   }

   private void updateValue(final T newValue) {
      Component newMessage = this.createLabelForValue(newValue);
      this.setMessage(newMessage);
      this.value = newValue;
      this.updateTooltip();
   }

   private Component createLabelForValue(final T newValue) {
      return (Component)(this.displayState == CycleButton$DisplayState.VALUE ? this.valueStringifier.apply(newValue) : this.createFullName(newValue));
   }

   private MutableComponent createFullName(final T newValue) {
      return CommonComponents.optionNameValue(this.name, this.valueStringifier.apply(newValue));
   }

   public T getValue() {
      return this.value;
   }

   @Override
   protected MutableComponent createNarrationMessage() {
      return this.narrationProvider.apply(this);
   }

   @Override
   public void updateWidgetNarration(final NarrationElementOutput output) {
      output.add(NarratedElementType.TITLE, this.createNarrationMessage());
      if (this.active) {
         T nextValue = this.getCycledValue(1);
         Component nextValueText = this.createLabelForValue(nextValue);
         if (this.isFocused()) {
            output.add(NarratedElementType.USAGE, Component.translatable("narration.cycle_button.usage.focused", nextValueText));
         } else {
            output.add(NarratedElementType.USAGE, Component.translatable("narration.cycle_button.usage.hovered", nextValueText));
         }
      }
   }

   public MutableComponent createDefaultNarrationMessage() {
      return wrapDefaultNarrationMessage((Component)(this.displayState == CycleButton$DisplayState.VALUE ? this.createFullName(this.value) : this.getMessage()));
   }

   public static <T> CycleButton$Builder<T> builder(final Function<T, Component> valueStringifier, final Supplier<T> defaultValueSupplier) {
      return new CycleButton$Builder<>(valueStringifier, defaultValueSupplier);
   }

   public static <T> CycleButton$Builder<T> builder(final Function<T, Component> valueStringifier, final T defaultValue) {
      return new CycleButton$Builder<>(valueStringifier, () -> defaultValue);
   }

   public static CycleButton$Builder<Boolean> booleanBuilder(final Component trueText, final Component falseText, final boolean defaultValue) {
      return new CycleButton$Builder<>(b -> b == Boolean.TRUE ? trueText : falseText, () -> defaultValue).withValues(BOOLEAN_OPTIONS);
   }

   public static CycleButton$Builder<Boolean> onOffBuilder(final boolean initialValue) {
      return new CycleButton$Builder<>(b -> b == Boolean.TRUE ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF, () -> initialValue)
         .withValues(BOOLEAN_OPTIONS);
   }
}
