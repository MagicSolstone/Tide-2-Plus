package net.minecraft.client.gui.components;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.OptionInstance$TooltipSupplier;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

@Environment(EnvType.CLIENT)
public class CycleButton$Builder<T> {
   private final Supplier<T> defaultValueSupplier;
   private final Function<T, Component> valueStringifier;
   private OptionInstance$TooltipSupplier<T> tooltipSupplier = value -> null;
   private CycleButton$SpriteSupplier<T> spriteSupplier = (button, value) -> null;
   private Function<CycleButton<T>, MutableComponent> narrationProvider = CycleButton::createDefaultNarrationMessage;
   private CycleButton$ValueListSupplier<T> values = CycleButton$ValueListSupplier.create(ImmutableList.of());
   private CycleButton$DisplayState displayState = CycleButton$DisplayState.NAME_AND_VALUE;

   public CycleButton$Builder(final Function<T, Component> valueStringifier, final Supplier<T> defaultValueSupplier) {
      this.valueStringifier = valueStringifier;
      this.defaultValueSupplier = defaultValueSupplier;
   }

   public CycleButton$Builder<T> withValues(final Collection<T> values) {
      return this.withValues(CycleButton$ValueListSupplier.create(values));
   }

   @SafeVarargs
   public final CycleButton$Builder<T> withValues(final T... values) {
      return this.withValues(ImmutableList.copyOf(values));
   }

   public CycleButton$Builder<T> withValues(final List<T> values, final List<T> altValues) {
      return this.withValues(CycleButton$ValueListSupplier.create(CycleButton.DEFAULT_ALT_LIST_SELECTOR, values, altValues));
   }

   public CycleButton$Builder<T> withValues(final BooleanSupplier altCondition, final List<T> values, final List<T> altValues) {
      return this.withValues(CycleButton$ValueListSupplier.create(altCondition, values, altValues));
   }

   public CycleButton$Builder<T> withValues(final CycleButton$ValueListSupplier<T> valueListSupplier) {
      this.values = valueListSupplier;
      return this;
   }

   public CycleButton$Builder<T> withTooltip(final OptionInstance$TooltipSupplier<T> tooltipSupplier) {
      this.tooltipSupplier = tooltipSupplier;
      return this;
   }

   public CycleButton$Builder<T> withCustomNarration(final Function<CycleButton<T>, MutableComponent> narrationProvider) {
      this.narrationProvider = narrationProvider;
      return this;
   }

   public CycleButton$Builder<T> withSprite(final CycleButton$SpriteSupplier<T> spriteSupplier) {
      this.spriteSupplier = spriteSupplier;
      return this;
   }

   public CycleButton$Builder<T> displayState(final CycleButton$DisplayState state) {
      this.displayState = state;
      return this;
   }

   public CycleButton$Builder<T> displayOnlyValue() {
      return this.displayState(CycleButton$DisplayState.VALUE);
   }

   public CycleButton<T> create(final Component name, final CycleButton$OnValueChange<T> valueChangeListener) {
      return this.create(0, 0, 150, 20, name, valueChangeListener);
   }

   public CycleButton<T> create(final int x, final int y, final int width, final int height, final Component name) {
      return this.create(x, y, width, height, name, (button, value) -> {});
   }

   public CycleButton<T> create(
      final int x, final int y, final int width, final int height, final Component name, final CycleButton$OnValueChange<T> valueChangeListener
   ) {
      List<T> values = this.values.getDefaultList();
      if (values.isEmpty()) {
         throw new IllegalStateException("No values for cycle button");
      } else {
         T initialValue = this.defaultValueSupplier.get();
         int initialIndex = values.indexOf(initialValue);
         Component valueText = this.valueStringifier.apply(initialValue);
         Component initialTitle = (Component)(this.displayState == CycleButton$DisplayState.VALUE
            ? valueText
            : CommonComponents.optionNameValue(name, valueText));
         return new CycleButton<>(
            x,
            y,
            width,
            height,
            initialTitle,
            name,
            initialIndex,
            initialValue,
            this.defaultValueSupplier,
            this.values,
            this.valueStringifier,
            this.narrationProvider,
            valueChangeListener,
            this.tooltipSupplier,
            this.displayState,
            this.spriteSupplier
         );
      }
   }
}
