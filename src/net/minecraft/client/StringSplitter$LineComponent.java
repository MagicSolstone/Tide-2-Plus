package net.minecraft.client;

import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.FormattedText$ContentConsumer;
import net.minecraft.network.chat.FormattedText$StyledContentConsumer;
import net.minecraft.network.chat.Style;

@Environment(EnvType.CLIENT)
class StringSplitter$LineComponent implements FormattedText {
   private final String contents;
   private final Style style;

   public StringSplitter$LineComponent(final String contents, final Style style) {
      this.contents = contents;
      this.style = style;
   }

   @Override
   public <T> Optional<T> visit(final FormattedText$ContentConsumer<T> output) {
      return output.accept(this.contents);
   }

   @Override
   public <T> Optional<T> visit(final FormattedText$StyledContentConsumer<T> output, final Style parentStyle) {
      return output.accept(this.style.applyTo(parentStyle), this.contents);
   }
}
