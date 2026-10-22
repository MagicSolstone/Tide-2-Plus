package net.minecraft;

import org.jspecify.annotations.Nullable;

public record CrashReportCategory$Entry(String key, String value) {
   public CrashReportCategory$Entry(final String key, @Nullable final Object rawValue) {
      String value;
      if (rawValue == null) {
         value = "~~NULL~~";
      } else if (rawValue instanceof Throwable t) {
         value = "~~ERROR~~ " + t.getClass().getSimpleName() + ": " + t.getMessage();
      } else {
         value = rawValue.toString();
      }

      this(key, value);
   }
}
