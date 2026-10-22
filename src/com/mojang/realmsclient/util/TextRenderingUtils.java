package com.mojang.realmsclient.util;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class TextRenderingUtils {
   private TextRenderingUtils() {
   }

   @VisibleForTesting
   protected static List<String> lineBreak(final String text) {
      return Arrays.asList(text.split("\\n"));
   }

   public static List<TextRenderingUtils$Line> decompose(final String text, final TextRenderingUtils$LineSegment... links) {
      return decompose(text, Arrays.asList(links));
   }

   private static List<TextRenderingUtils$Line> decompose(final String text, final List<TextRenderingUtils$LineSegment> links) {
      List<String> brokenLines = lineBreak(text);
      return insertLinks(brokenLines, links);
   }

   private static List<TextRenderingUtils$Line> insertLinks(final List<String> lines, final List<TextRenderingUtils$LineSegment> links) {
      int linkCount = 0;
      List<TextRenderingUtils$Line> processedLines = Lists.newArrayList();

      for (String line : lines) {
         List<TextRenderingUtils$LineSegment> segments = Lists.newArrayList();

         for (String part : split(line, "%link")) {
            if ("%link".equals(part)) {
               segments.add(links.get(linkCount++));
            } else {
               segments.add(TextRenderingUtils$LineSegment.text(part));
            }
         }

         processedLines.add(new TextRenderingUtils$Line(segments));
      }

      return processedLines;
   }

   public static List<String> split(final String line, final String delimiter) {
      if (delimiter.isEmpty()) {
         throw new IllegalArgumentException("Delimiter cannot be the empty string");
      } else {
         List<String> parts = Lists.newArrayList();
         int searchStart = 0;

         int matchIndex;
         while ((matchIndex = line.indexOf(delimiter, searchStart)) != -1) {
            if (matchIndex > searchStart) {
               parts.add(line.substring(searchStart, matchIndex));
            }

            parts.add(delimiter);
            searchStart = matchIndex + delimiter.length();
         }

         if (searchStart < line.length()) {
            parts.add(line.substring(searchStart));
         }

         return parts;
      }
   }
}
