package com.mojang.realmsclient.util;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class TextRenderingUtils$Line {
   public final List<TextRenderingUtils$LineSegment> segments;

   public TextRenderingUtils$Line(final TextRenderingUtils$LineSegment... segments) {
      this(Arrays.asList(segments));
   }

   public TextRenderingUtils$Line(final List<TextRenderingUtils$LineSegment> segments) {
      this.segments = segments;
   }

   @Override
   public String toString() {
      return "Line{segments=" + this.segments + "}";
   }

   @Override
   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         TextRenderingUtils$Line line = (TextRenderingUtils$Line)o;
         return Objects.equals(this.segments, line.segments);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.segments);
   }
}
