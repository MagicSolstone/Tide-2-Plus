package net.minecraft.network.chat;

import java.util.Optional;

class FormattedText$2 implements FormattedText {
   FormattedText$2(final String val$text) {
      this.val$text = val$textx;
   }

   @Override
   public <T> Optional<T> visit(final FormattedText$ContentConsumer<T> output) {
      return output.accept(this.val$text);
   }

   @Override
   public <T> Optional<T> visit(final FormattedText$StyledContentConsumer<T> output, final Style parentStyle) {
      return output.accept(parentStyle, this.val$text);
   }
}
