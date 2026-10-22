package net.minecraft.locale;

import java.util.Map;
import java.util.Optional;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.StringDecomposer;

class Language$1 extends Language {
   Language$1(final Map val$storage) {
      this.val$storage = val$storagex;
   }

   @Override
   public String getOrDefault(final String elementId, final String defaultValue) {
      return this.val$storage.getOrDefault(elementId, defaultValue);
   }

   @Override
   public boolean has(final String elementId) {
      return this.val$storage.containsKey(elementId);
   }

   @Override
   public boolean isDefaultRightToLeft() {
      return false;
   }

   @Override
   public FormattedCharSequence getVisualOrder(final FormattedText logicalOrderText) {
      return output -> logicalOrderText.visit(
            (style, contents) -> StringDecomposer.iterateFormatted(contents, style, output) ? Optional.empty() : FormattedText.STOP_ITERATION, Style.EMPTY
         )
         .isPresent();
   }
}
