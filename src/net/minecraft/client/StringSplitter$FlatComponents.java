package net.minecraft.client;

import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class StringSplitter$FlatComponents {
   private final List<StringSplitter$LineComponent> parts;
   private String flatParts;

   public StringSplitter$FlatComponents(final List<StringSplitter$LineComponent> parts) {
      this.parts = parts;
      this.flatParts = parts.stream().map(p -> p.contents).collect(Collectors.joining());
   }

   public char charAt(final int position) {
      return this.flatParts.charAt(position);
   }

   public FormattedText splitAt(final int skipPosition, final int skipSize, final Style splitStyle) {
      ComponentCollector result = new ComponentCollector();
      ListIterator<StringSplitter$LineComponent> it = this.parts.listIterator();
      int position = skipPosition;
      boolean inSkip = false;

      while (it.hasNext()) {
         StringSplitter$LineComponent element = it.next();
         String contents = element.contents;
         int contentsSize = contents.length();
         if (!inSkip) {
            if (position > contentsSize) {
               result.append(element);
               it.remove();
               position -= contentsSize;
            } else {
               String beforeSplit = contents.substring(0, position);
               if (!beforeSplit.isEmpty()) {
                  result.append(FormattedText.of(beforeSplit, element.style));
               }

               position += skipSize;
               inSkip = true;
            }
         }

         if (inSkip) {
            if (position <= contentsSize) {
               String afterSplit = contents.substring(position);
               if (afterSplit.isEmpty()) {
                  it.remove();
               } else {
                  it.set(new StringSplitter$LineComponent(afterSplit, splitStyle));
               }
               break;
            }

            it.remove();
            position -= contentsSize;
         }
      }

      this.flatParts = this.flatParts.substring(skipPosition + skipSize);
      return result.getResultOrEmpty();
   }

   @Nullable
   public FormattedText getRemainder() {
      ComponentCollector result = new ComponentCollector();
      this.parts.forEach(result::append);
      this.parts.clear();
      return result.getResult();
   }
}
