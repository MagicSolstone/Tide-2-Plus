package net.minecraft.client;

import com.google.common.collect.ImmutableList;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import java.util.Objects;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public final class OptionInstance<T> {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final OptionInstance$Enum<Boolean> BOOLEAN_VALUES = new OptionInstance$Enum<>(ImmutableList.of(Boolean.TRUE, Boolean.FALSE), Codec.BOOL);
   public static final OptionInstance$CaptionBasedToString<Boolean> BOOLEAN_TO_STRING = (var0, b) -> b
      ? CommonComponents.OPTION_ON
      : CommonComponents.OPTION_OFF;
   public static final OptionInstance$ValueUpdateListener<Object> NO_ACTION = var0 -> {};
   private final OptionInstance$TooltipSupplier<T> tooltip;
   private final Function<T, Component> toString;
   private final OptionInstance$ValueSet<T> values;
   private final Codec<T> codec;
   private final T initialValue;
   private final OptionInstance$ValueUpdateListener<? super T> onValueUpdate;
   private final Component caption;
   private T value;

   public static OptionInstance<Boolean> createBoolean(
      final String captionId, final boolean initialValue, final OptionInstance$ValueUpdateListener<? super Boolean> onValueUpdate
   ) {
      return createBoolean(captionId, noTooltip(), initialValue, onValueUpdate);
   }

   public static OptionInstance<Boolean> createBoolean(final String captionId, final boolean initialValue) {
      return createBoolean(captionId, noTooltip(), initialValue, NO_ACTION);
   }

   public static OptionInstance<Boolean> createBoolean(
      final String captionId, final OptionInstance$TooltipSupplier<Boolean> tooltip, final boolean initialValue
   ) {
      return createBoolean(captionId, tooltip, initialValue, NO_ACTION);
   }

   public static OptionInstance<Boolean> createBoolean(
      final String captionId,
      final OptionInstance$TooltipSupplier<Boolean> tooltip,
      final boolean initialValue,
      final OptionInstance$ValueUpdateListener<? super Boolean> onValueUpdate
   ) {
      return createBoolean(captionId, tooltip, BOOLEAN_TO_STRING, initialValue, onValueUpdate);
   }

   public static OptionInstance<Boolean> createBoolean(
      final String captionId,
      final OptionInstance$TooltipSupplier<Boolean> tooltip,
      final OptionInstance$CaptionBasedToString<Boolean> toString,
      final boolean initialValue,
      final OptionInstance$ValueUpdateListener<? super Boolean> onValueUpdate
   ) {
      return new OptionInstance<>(captionId, tooltip, toString, BOOLEAN_VALUES, initialValue, onValueUpdate);
   }

   public OptionInstance(
      final String captionId,
      final OptionInstance$TooltipSupplier<T> tooltip,
      final OptionInstance$CaptionBasedToString<T> toString,
      final OptionInstance$ValueSet<T> values,
      final T initialValue,
      final OptionInstance$ValueUpdateListener<? super T> onValueUpdate
   ) {
      this(captionId, tooltip, toString, values, values.codec(), initialValue, onValueUpdate);
   }

   public OptionInstance(
      final String captionId,
      final OptionInstance$TooltipSupplier<T> tooltip,
      final OptionInstance$CaptionBasedToString<T> toString,
      final OptionInstance$ValueSet<T> values,
      final Codec<T> codec,
      final T initialValue,
      final OptionInstance$ValueUpdateListener<? super T> onValueUpdate
   ) {
      this.caption = Component.translatable(captionId);
      this.tooltip = tooltip;
      this.toString = value -> toString.toString(this.caption, value);
      this.values = values;
      this.codec = codec;
      this.initialValue = initialValue;
      this.onValueUpdate = onValueUpdate;
      this.value = this.initialValue;
   }

   public static <T> OptionInstance$TooltipSupplier<T> noTooltip() {
      return var0 -> null;
   }

   public static <T> OptionInstance$TooltipSupplier<T> cachedConstantTooltip(final Component tooltipComponent) {
      return var1 -> Tooltip.create(tooltipComponent);
   }

   public AbstractWidget createButton(final Options options) {
      return this.createButton(options, 0, 0, 150);
   }

   public AbstractWidget createButton(final Options options, final int x, final int y, final int width) {
      return this.createButton(options, x, y, width, NO_ACTION);
   }

   public AbstractWidget createButton(
      final Options options, final int x, final int y, final int width, final OptionInstance$ValueUpdateListener<? super T> onValueChanged
   ) {
      return this.values.createButton(this.tooltip, options, x, y, width, onValueChanged).apply(this);
   }

   public T get() {
      return this.value;
   }

   public Codec<T> codec() {
      return this.codec;
   }

   @Override
   public String toString() {
      return this.caption.getString();
   }

   public void set(final T value) {
      T newValue = this.values.validateValue(value).orElseGet(() -> {
         LOGGER.error("Illegal option value {} for {}", value, this.caption.getString());
         return this.initialValue;
      });
      if (!Minecraft.getInstance().isRunning()) {
         this.value = newValue;
      } else {
         if (!Objects.equals(this.value, newValue)) {
            this.value = newValue;
            this.onValueUpdate.valueChanged(newValue);
         }
      }
   }

   public OptionInstance$ValueSet<T> values() {
      return this.values;
   }
}
