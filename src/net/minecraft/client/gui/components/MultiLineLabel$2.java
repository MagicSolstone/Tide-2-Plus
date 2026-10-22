package net.minecraft.client.gui.components;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class MultiLineLabel$2 implements MultiLineLabel {
   @Nullable
   private List<MultiLineLabel$TextAndWidth> cachedTextAndWidth;
   @Nullable
   private Language splitWithLanguage;

   MultiLineLabel$2(final Component[] val$messages, final Font val$font, final int val$maxWidth, final int val$maxLines) {
      this.val$messages = val$messagesx;
      this.val$font = val$fontx;
      this.val$maxWidth = val$maxWidthx;
      this.val$maxLines = val$maxLinesx;
   }

   @Override
   public int visitLines(final TextAlignment align, final int anchorX, final int topY, final int lineHeight, final ActiveTextCollector output) {
      int y = topY;

      for (MultiLineLabel$TextAndWidth splitLine : this.getSplitMessage()) {
         int leftX = align.calculateLeft(anchorX, splitLine.width);
         output.accept(leftX, y, splitLine.text);
         y += lineHeight;
      }

      return y;
   }

   private List<MultiLineLabel$TextAndWidth> getSplitMessage() {
      Language currentLanguage = Language.getInstance();
      if (this.cachedTextAndWidth != null && currentLanguage == this.splitWithLanguage) {
         return this.cachedTextAndWidth;
      } else {
         this.splitWithLanguage = currentLanguage;
         List<FormattedText> splitMessage = new ArrayList<>();

         for (Component message : this.val$messages) {
            splitMessage.addAll(this.val$font.splitIgnoringLanguage(message, this.val$maxWidth));
         }

         this.cachedTextAndWidth = new ArrayList<>();
         int actualMaxLines = Math.min(splitMessage.size(), this.val$maxLines);
         List<FormattedText> linesToAdd = splitMessage.subList(0, actualMaxLines);

         for (int i = 0; i < linesToAdd.size(); i++) {
            FormattedText formattedText = linesToAdd.get(i);
            FormattedCharSequence formattedCharSequence = Language.getInstance().getVisualOrder(formattedText);
            if (i == linesToAdd.size() - 1 && actualMaxLines == this.val$maxLines && actualMaxLines != splitMessage.size()) {
               FormattedText clippedText = this.val$font
                  .substrByWidth(formattedText, this.val$font.width(formattedText) - this.val$font.width(CommonComponents.ELLIPSIS));
               FormattedText withEllipsis = FormattedText.composite(
                  clippedText, CommonComponents.ELLIPSIS.copy().withStyle(this.val$messages[this.val$messages.length - 1].getStyle())
               );
               this.cachedTextAndWidth
                  .add(new MultiLineLabel$TextAndWidth(Language.getInstance().getVisualOrder(withEllipsis), this.val$font.width(withEllipsis)));
            } else {
               this.cachedTextAndWidth.add(new MultiLineLabel$TextAndWidth(formattedCharSequence, this.val$font.width(formattedCharSequence)));
            }
         }

         return this.cachedTextAndWidth;
      }
   }

   @Override
   public int getLineCount() {
      return this.getSplitMessage().size();
   }

   @Override
   public int getWidth() {
      return Math.min(this.val$maxWidth, this.getSplitMessage().stream().mapToInt(MultiLineLabel$TextAndWidth::width).max().orElse(0));
   }
}
