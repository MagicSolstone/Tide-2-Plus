package net.minecraft.client;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.StringDecomposer;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class StringSplitter {
   private final StringSplitter$WidthProvider widthProvider;

   public StringSplitter(final StringSplitter$WidthProvider widthProvider) {
      this.widthProvider = widthProvider;
   }

   public float stringWidth(@Nullable final String str) {
      if (str == null) {
         return 0.0F;
      } else {
         MutableFloat result = new MutableFloat();
         StringDecomposer.iterateFormatted(str, Style.EMPTY, (position, style, codepoint) -> {
            result.add(this.widthProvider.getWidth(codepoint, style));
            return true;
         });
         return result.floatValue();
      }
   }

   public float stringWidth(final FormattedText text) {
      MutableFloat result = new MutableFloat();
      StringDecomposer.iterateFormatted(text, Style.EMPTY, (position, style, codepoint) -> {
         result.add(this.widthProvider.getWidth(codepoint, style));
         return true;
      });
      return result.floatValue();
   }

   public float stringWidth(final FormattedCharSequence text) {
      MutableFloat result = new MutableFloat();
      text.accept((position, style, codepoint) -> {
         result.add(this.widthProvider.getWidth(codepoint, style));
         return true;
      });
      return result.floatValue();
   }

   public int plainIndexAtWidth(final String str, final int maxWidth, final Style style) {
      StringSplitter$WidthLimitedCharSink output = new StringSplitter$WidthLimitedCharSink(this, maxWidth);
      StringDecomposer.iterate(str, style, output);
      return output.getPosition();
   }

   public String plainHeadByWidth(final String str, final int maxWidth, final Style style) {
      return str.substring(0, this.plainIndexAtWidth(str, maxWidth, style));
   }

   public String plainTailByWidth(final String str, final int maxWidth, final Style style) {
      MutableFloat currentWidth = new MutableFloat();
      MutableInt result = new MutableInt(str.length());
      StringDecomposer.iterateBackwards(str, style, (position, s, codepoint) -> {
         float w = currentWidth.addAndGet(this.widthProvider.getWidth(codepoint, s));
         if (w > maxWidth) {
            return false;
         } else {
            result.setValue(position);
            return true;
         }
      });
      return str.substring(result.intValue());
   }

   public FormattedText headByWidth(final FormattedText text, final int width, final Style initialStyle) {
      StringSplitter$WidthLimitedCharSink output = new StringSplitter$WidthLimitedCharSink(this, width);
      return text.visit(new StringSplitter$1(this, output), initialStyle).orElse(text);
   }

   public int findLineBreak(final String input, final int max, final Style initialStyle) {
      StringSplitter$LineBreakFinder finder = new StringSplitter$LineBreakFinder(this, max);
      StringDecomposer.iterateFormatted(input, initialStyle, finder);
      return finder.getSplitPosition();
   }

   public static int getWordPosition(final String text, final int dir, final int from, final boolean stripSpaces) {
      int result = from;
      boolean reverse = dir < 0;
      int abs = Math.abs(dir);

      for (int i = 0; i < abs; i++) {
         if (reverse) {
            while (stripSpaces && result > 0 && (text.charAt(result - 1) == ' ' || text.charAt(result - 1) == '\n')) {
               result--;
            }

            while (result > 0 && text.charAt(result - 1) != ' ' && text.charAt(result - 1) != '\n') {
               result--;
            }
         } else {
            int length = text.length();
            int index1 = text.indexOf(32, result);
            int index2 = text.indexOf(10, result);
            if (index1 == -1 && index2 == -1) {
               result = -1;
            } else if (index1 != -1 && index2 != -1) {
               result = Math.min(index1, index2);
            } else if (index1 != -1) {
               result = index1;
            } else {
               result = index2;
            }

            if (result == -1) {
               result = length;
            } else {
               while (stripSpaces && result < length && (text.charAt(result) == ' ' || text.charAt(result) == '\n')) {
                  result++;
               }
            }
         }
      }

      return result;
   }

   public void splitLines(
      final String input, final int maxWidth, final Style initialStyle, final boolean includeAll, final StringSplitter$LinePosConsumer output
   ) {
      int start = 0;
      int size = input.length();
      Style workStyle = initialStyle;

      while (start < size) {
         StringSplitter$LineBreakFinder finder = new StringSplitter$LineBreakFinder(this, maxWidth);
         boolean endOfText = StringDecomposer.iterateFormatted(input, start, workStyle, initialStyle, finder);
         if (endOfText) {
            output.accept(workStyle, start, size);
            break;
         }

         int lineBreak = finder.getSplitPosition();
         char firstTailChar = input.charAt(lineBreak);
         int adjustedBreak = firstTailChar != '\n' && firstTailChar != ' ' ? lineBreak : lineBreak + 1;
         output.accept(workStyle, start, includeAll ? adjustedBreak : lineBreak);
         start = adjustedBreak;
         workStyle = finder.getSplitStyle();
      }
   }

   public List<FormattedText> splitLines(final String input, final int maxWidth, final Style initialStyle) {
      List<FormattedText> result = Lists.newArrayList();
      this.splitLines(input, maxWidth, initialStyle, false, (style, start, end) -> result.add(FormattedText.of(input.substring(start, end), style)));
      return result;
   }

   public List<FormattedText> splitLines(final FormattedText input, final int maxWidth, final Style initialStyle) {
      List<FormattedText> result = Lists.newArrayList();
      this.splitLines(input, maxWidth, initialStyle, (text, wrapped) -> result.add(text));
      return result;
   }

   public void splitLines(final FormattedText input, final int maxWidth, final Style initialStyle, final BiConsumer<FormattedText, Boolean> output) {
      List<StringSplitter$LineComponent> partList = Lists.newArrayList();
      input.visit((style, contents) -> {
         if (!contents.isEmpty()) {
            partList.add(new StringSplitter$LineComponent(contents, style));
         }

         return Optional.empty();
      }, initialStyle);
      StringSplitter$FlatComponents parts = new StringSplitter$FlatComponents(partList);
      boolean shouldRestart = true;
      boolean forceNewLine = false;
      boolean isWrapped = false;

      while (shouldRestart) {
         shouldRestart = false;
         StringSplitter$LineBreakFinder finder = new StringSplitter$LineBreakFinder(this, maxWidth);

         for (StringSplitter$LineComponent part : parts.parts) {
            boolean endOfText = StringDecomposer.iterateFormatted(part.contents, 0, part.style, initialStyle, finder);
            if (!endOfText) {
               int lineBreak = finder.getSplitPosition();
               Style lineBreakStyle = finder.getSplitStyle();
               char firstTailChar = parts.charAt(lineBreak);
               boolean isNewLine = firstTailChar == '\n';
               boolean skipNextChar = isNewLine || firstTailChar == ' ';
               forceNewLine = isNewLine;
               FormattedText result = parts.splitAt(lineBreak, skipNextChar ? 1 : 0, lineBreakStyle);
               output.accept(result, isWrapped);
               isWrapped = !isNewLine;
               shouldRestart = true;
               break;
            }

            finder.addToOffset(part.contents.length());
         }
      }

      FormattedText lastLine = parts.getRemainder();
      if (lastLine != null) {
         output.accept(lastLine, isWrapped);
      } else if (forceNewLine) {
         output.accept(FormattedText.EMPTY, false);
      }
   }
}
