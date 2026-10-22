package net.minecraft.network.chat;

import java.util.Optional;

class FormattedText$3 implements FormattedText {
   FormattedText$3(final String val$text, final Style val$style) {
      this.val$text = val$textx;
      this.val$style = val$stylex;
   }

   @Override
   public <T> Optional<T> visit(final FormattedText$ContentConsumer<T> output) {
      return output.accept(this.val$text);
   }

   @Override
   public <T> Optional<T> visit(final FormattedText$StyledContentConsumer<T> output, final Style parentStyle) {
      return output.accept(this.val$style.applyTo(parentStyle), this.val$text);
   }
}
