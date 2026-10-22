package net.minecraft.util.eventlog;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventLogDirectory$FileList implements Iterable<EventLogDirectory$File> {
   private final List<EventLogDirectory$File> files;

   private EventLogDirectory$FileList(final List<EventLogDirectory$File> files) {
      this.files = new ArrayList<>(files);
   }

   public EventLogDirectory$FileList prune(final LocalDate date, final int expiryDays) {
      this.files.removeIf(file -> {
         EventLogDirectory$FileId id = file.id();
         LocalDate expiresAt = id.date().plusDays(expiryDays);
         if (!date.isBefore(expiresAt)) {
            try {
               Files.delete(file.path());
               return true;
            } catch (IOException var6) {
               EventLogDirectory.LOGGER.warn("Failed to delete expired event log file: {}", file.path(), var6);
            }
         }

         return false;
      });
      return this;
   }

   public EventLogDirectory$FileList compressAll() {
      ListIterator<EventLogDirectory$File> iterator = this.files.listIterator();

      while (iterator.hasNext()) {
         EventLogDirectory$File file = iterator.next();

         try {
            iterator.set(file.compress());
         } catch (IOException var4) {
            EventLogDirectory.LOGGER.warn("Failed to compress event log file: {}", file.path(), var4);
         }
      }

      return this;
   }

   @Override
   public Iterator<EventLogDirectory$File> iterator() {
      return this.files.iterator();
   }

   public Stream<EventLogDirectory$File> stream() {
      return this.files.stream();
   }

   public Set<EventLogDirectory$FileId> ids() {
      return this.files.stream().map(EventLogDirectory$File::id).collect(Collectors.toSet());
   }
}
