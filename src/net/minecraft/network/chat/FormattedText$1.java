package net.minecraft.network.chat;

import java.util.Optional;

class FormattedText$1 implements FormattedText {
   @Override
   public <T> Optional<T> visit(final FormattedText$ContentConsumer<T> output) {
      return Optional.empty();
   }

   @Override
   public <T> Optional<T> visit(final FormattedText$StyledContentConsumer<T> output, final Style parentStyle) {
      return Optional.empty();
   }
}
