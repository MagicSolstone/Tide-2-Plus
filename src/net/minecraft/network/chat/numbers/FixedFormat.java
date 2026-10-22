package net.minecraft.network.chat.numbers;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public record FixedFormat(Component value) implements NumberFormat {
   public static final NumberFormatType<FixedFormat> TYPE = new FixedFormat$1();

   @Override
   public MutableComponent format(final int value) {
      return this.value.copy();
   }

   @Override
   public NumberFormatType<FixedFormat> type() {
      return TYPE;
   }
}
