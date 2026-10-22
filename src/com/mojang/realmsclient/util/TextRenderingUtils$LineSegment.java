package com.mojang.realmsclient.util;

import com.google.common.annotations.VisibleForTesting;
import java.net.URI;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class TextRenderingUtils$LineSegment {
   private final String fullText;
   @Nullable
   private final String linkTitle;
   @Nullable
   private final URI linkUrl;

   private TextRenderingUtils$LineSegment(final String fullText) {
      this.fullText = fullText;
      this.linkTitle = null;
      this.linkUrl = null;
   }

   private TextRenderingUtils$LineSegment(final String fullText, @Nullable final String linkTitle, @Nullable final URI linkUrl) {
      this.fullText = fullText;
      this.linkTitle = linkTitle;
      this.linkUrl = linkUrl;
   }

   @Override
   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         TextRenderingUtils$LineSegment segment = (TextRenderingUtils$LineSegment)o;
         return Objects.equals(this.fullText, segment.fullText)
            && Objects.equals(this.linkTitle, segment.linkTitle)
            && Objects.equals(this.linkUrl, segment.linkUrl);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.fullText, this.linkTitle, this.linkUrl);
   }

   @Override
   public String toString() {
      return "Segment{fullText='" + this.fullText + "', linkTitle='" + this.linkTitle + "', linkUrl='" + this.linkUrl + "'}";
   }

   public String renderedText() {
      return this.isLink() ? this.linkTitle : this.fullText;
   }

   public boolean isLink() {
      return this.linkTitle != null;
   }

   public URI getLinkUrl() {
      if (!this.isLink()) {
         throw new IllegalStateException("Not a link: " + this);
      } else {
         return this.linkUrl;
      }
   }

   public static TextRenderingUtils$LineSegment link(final String linkTitle, final URI linkUrl) {
      return new TextRenderingUtils$LineSegment(null, linkTitle, linkUrl);
   }

   @VisibleForTesting
   protected static TextRenderingUtils$LineSegment text(final String fullText) {
      return new TextRenderingUtils$LineSegment(fullText);
   }
}
