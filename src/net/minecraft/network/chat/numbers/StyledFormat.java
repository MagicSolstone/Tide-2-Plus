package net.minecraft.network.chat.numbers;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

public record StyledFormat(Style style) implements NumberFormat {
   public static final NumberFormatType<StyledFormat> TYPE = new StyledFormat$1();
   public static final StyledFormat NO_STYLE = new StyledFormat(Style.EMPTY);
   public static final StyledFormat SIDEBAR_DEFAULT = new StyledFormat(Style.EMPTY.withColor(ChatFormatting.RED));
   public static final StyledFormat PLAYER_LIST_DEFAULT = new StyledFormat(Style.EMPTY.withColor(ChatFormatting.YELLOW));

   @Override
   public MutableComponent format(final int value) {
      return Component.literal(Integer.toString(value)).withStyle(this.style);
   }

   @Override
   public NumberFormatType<StyledFormat> type() {
      return TYPE;
   }
}
