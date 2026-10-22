package net.minecraft.nbt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public class TextComponentTagVisitor$RichStyling implements TextComponentTagVisitor$Styling {
   private static final Style SYNTAX_HIGHLIGHTING_NUMBER_TYPE = Style.EMPTY.withColor(ChatFormatting.RED);
   public static final TextComponentTagVisitor$Styling INSTANCE = new TextComponentTagVisitor$RichStyling();
   private final Map<TextComponentTagVisitor$Token, Component> tokens = new HashMap<>();
   private static final Style SYNTAX_HIGHLIGHTING_KEY = Style.EMPTY.withColor(ChatFormatting.AQUA);
   private static final Style SYNTAX_HIGHLIGHTING_STRING = Style.EMPTY.withColor(ChatFormatting.GREEN);
   private static final Style SYNTAX_HIGHLIGHTING_NUMBER = Style.EMPTY.withColor(ChatFormatting.GOLD);

   private TextComponentTagVisitor$RichStyling() {
      this.overrideToken(TextComponentTagVisitor$Token.FOLDED, Style.EMPTY.withColor(ChatFormatting.GRAY));
      this.overrideToken(TextComponentTagVisitor$Token.BYTE_SUFFIX, SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
      this.overrideToken(TextComponentTagVisitor$Token.BYTE_ARRAY_PREFIX, SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
      this.overrideToken(TextComponentTagVisitor$Token.SHORT_SUFFIX, SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
      this.overrideToken(TextComponentTagVisitor$Token.INT_ARRAY_PREFIX, SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
      this.overrideToken(TextComponentTagVisitor$Token.LONG_SUFFIX, SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
      this.overrideToken(TextComponentTagVisitor$Token.LONG_ARRAY_PREFIX, SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
      this.overrideToken(TextComponentTagVisitor$Token.FLOAT_SUFFIX, SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
      this.overrideToken(TextComponentTagVisitor$Token.DOUBLE_SUFFIX, SYNTAX_HIGHLIGHTING_NUMBER_TYPE);

      for (TextComponentTagVisitor$Token value : TextComponentTagVisitor$Token.values()) {
         this.tokens.putIfAbsent(value, Component.literal(value.text));
      }
   }

   private void overrideToken(final TextComponentTagVisitor$Token token, final Style style) {
      this.tokens.put(token, Component.literal(token.text).withStyle(style));
   }

   @Override
   public Style keyStyle() {
      return SYNTAX_HIGHLIGHTING_KEY;
   }

   @Override
   public Style stringStyle() {
      return SYNTAX_HIGHLIGHTING_STRING;
   }

   @Override
   public Style numberStyle() {
      return SYNTAX_HIGHLIGHTING_NUMBER;
   }

   @Override
   public Component token(final TextComponentTagVisitor$Token token) {
      return Objects.requireNonNull(this.tokens.get(token));
   }
}
