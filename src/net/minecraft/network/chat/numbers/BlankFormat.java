package net.minecraft.network.chat.numbers;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class BlankFormat implements NumberFormat {
   public static final BlankFormat INSTANCE = new BlankFormat();
   public static final NumberFormatType<BlankFormat> TYPE = new BlankFormat$1();

   private BlankFormat() {
   }

   @Override
   public MutableComponent format(final int value) {
      return Component.empty();
   }

   @Override
   public NumberFormatType<BlankFormat> type() {
      return TYPE;
   }
}
