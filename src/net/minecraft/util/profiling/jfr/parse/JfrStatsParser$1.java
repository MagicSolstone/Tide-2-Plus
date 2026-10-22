package net.minecraft.util.profiling.jfr.parse;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingFile;

class JfrStatsParser$1 implements Iterator<RecordedEvent> {
   JfrStatsParser$1(final RecordingFile val$recordingFile) {
      this.val$recordingFile = val$recordingFilex;
   }

   @Override
   public boolean hasNext() {
      return this.val$recordingFile.hasMoreEvents();
   }

   public RecordedEvent next() {
      if (!this.hasNext()) {
         throw new NoSuchElementException();
      } else {
         try {
            return this.val$recordingFile.readEvent();
         } catch (IOException var2) {
            throw new UncheckedIOException(var2);
         }
      }
   }
}
