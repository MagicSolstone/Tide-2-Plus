package net.minecraft.client;

import java.util.Objects;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.FormattedText$StyledContentConsumer;
import net.minecraft.network.chat.Style;
import net.minecraft.util.StringDecomposer;

@Environment(EnvType.CLIENT)
class StringSplitter$1 implements FormattedText$StyledContentConsumer<FormattedText> {
   private final ComponentCollector collector;

   StringSplitter$1(final StringSplitter this$0, final StringSplitter$WidthLimitedCharSink val$output) {
      this.val$output = val$outputx;
      Objects.requireNonNull(this$0);
      super();
      this.collector = new ComponentCollector();
   }

   @Override
   public Optional<FormattedText> accept(final Style style, final String contents) {
      this.val$output.resetPosition();
      if (!StringDecomposer.iterateFormatted(contents, style, this.val$output)) {
         String partial = contents.substring(0, this.val$output.getPosition());
         if (!partial.isEmpty()) {
            this.collector.append(FormattedText.of(partial, style));
         }

         return Optional.of(this.collector.getResultOrEmpty());
      } else {
         if (!contents.isEmpty()) {
            this.collector.append(FormattedText.of(contents, style));
         }

         return Optional.empty();
      }
   }
}
