package net.minecraft.util;

public enum ModCheck$Confidence {
   PROBABLY_NOT("Probably not.", false),
   VERY_LIKELY("Very likely;", true),
   DEFINITELY("Definitely;", true);

   private final String description;
   private final boolean shouldReportAsModified;

   private ModCheck$Confidence(final String description, final boolean shouldReportAsModified) {
      this.description = description;
      this.shouldReportAsModified = shouldReportAsModified;
   }
}
