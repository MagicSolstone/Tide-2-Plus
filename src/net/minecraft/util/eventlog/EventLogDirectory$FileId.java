package net.minecraft.util.eventlog;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.jspecify.annotations.Nullable;

public record EventLogDirectory$FileId(LocalDate date, int index) {
   private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;

   @Nullable
   public static EventLogDirectory$FileId parse(final String name) {
      int separator = name.indexOf("-");
      if (separator == -1) {
         return null;
      } else {
         String date = name.substring(0, separator);
         String index = name.substring(separator + 1);

         try {
            return new EventLogDirectory$FileId(LocalDate.parse(date, DATE_FORMATTER), Integer.parseInt(index));
         } catch (DateTimeParseException | NumberFormatException var5) {
            return null;
         }
      }
   }

   @Override
   public String toString() {
      return DATE_FORMATTER.format(this.date) + "-" + this.index;
   }

   public String toFileName(final String extension) {
      return this + extension;
   }
}
