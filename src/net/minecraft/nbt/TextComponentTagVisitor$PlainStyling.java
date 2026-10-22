package net.minecraft.nbt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public class TextComponentTagVisitor$PlainStyling implements TextComponentTagVisitor$Styling {
   public static final TextComponentTagVisitor$Styling INSTANCE = new TextComponentTagVisitor$PlainStyling();
   private final Map<TextComponentTagVisitor$Token, Component> tokens = new HashMap<>();

   private TextComponentTagVisitor$PlainStyling() {
      for (TextComponentTagVisitor$Token value : TextComponentTagVisitor$Token.values()) {
         this.tokens.put(value, Component.literal(value.text));
      }
   }

   @Override
   public Style keyStyle() {
      return Style.EMPTY;
   }

   @Override
   public Style stringStyle() {
      return Style.EMPTY;
   }

   @Override
   public Style numberStyle() {
      return Style.EMPTY;
   }

   @Override
   public Component token(final TextComponentTagVisitor$Token token) {
      return Objects.requireNonNull(this.tokens.get(token));
   }
}
