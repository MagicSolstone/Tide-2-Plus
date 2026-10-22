package net.minecraft.network.chat;

import java.util.List;
import java.util.Optional;

class FormattedText$4 implements FormattedText {
   FormattedText$4(final List val$parts) {
      this.val$parts = val$partsx;
   }

   @Override
   public <T> Optional<T> visit(final FormattedText$ContentConsumer<T> output) {
      for (FormattedText part : this.val$parts) {
         Optional<T> result = part.visit(output);
         if (result.isPresent()) {
            return result;
         }
      }

      return Optional.empty();
   }

   @Override
   public <T> Optional<T> visit(final FormattedText$StyledContentConsumer<T> output, final Style parentStyle) {
      for (FormattedText part : this.val$parts) {
         Optional<T> result = part.visit(output, parentStyle);
         if (result.isPresent()) {
            return result;
         }
      }

      return Optional.empty();
   }
}
