package net.minecraft.world.entity.ai.attributes;

import net.minecraft.ChatFormatting;

public enum Attribute$Sentiment {
   POSITIVE,
   NEUTRAL,
   NEGATIVE;

   public ChatFormatting getStyle(final boolean valueIncrease) {
      return switch (this) {
         case POSITIVE -> valueIncrease ? ChatFormatting.BLUE : ChatFormatting.RED;
         case NEUTRAL -> ChatFormatting.GRAY;
         case NEGATIVE -> valueIncrease ? ChatFormatting.RED : ChatFormatting.BLUE;
      };
   }
}
