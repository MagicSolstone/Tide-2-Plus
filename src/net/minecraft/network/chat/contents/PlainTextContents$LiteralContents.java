package net.minecraft.network.chat.contents;

import java.util.Optional;
import net.minecraft.network.chat.FormattedText$ContentConsumer;
import net.minecraft.network.chat.FormattedText$StyledContentConsumer;
import net.minecraft.network.chat.Style;

public record PlainTextContents$LiteralContents(String text) implements PlainTextContents {
   @Override
   public <T> Optional<T> visit(final FormattedText$ContentConsumer<T> output) {
      return output.accept(this.text);
   }

   @Override
   public <T> Optional<T> visit(final FormattedText$StyledContentConsumer<T> output, final Style currentStyle) {
      return output.accept(currentStyle, this.text);
   }

   @Override
   public String toString() {
      return "literal{" + this.text + "}";
   }
}
