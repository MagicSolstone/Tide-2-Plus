package net.minecraft.util.profiling.jfr;

import java.util.Objects;
import jdk.jfr.FlightRecorder;
import jdk.jfr.FlightRecorderListener;
import jdk.jfr.Recording;

class JfrProfiler$2 implements FlightRecorderListener {
   private final SummaryReporter summaryReporter;

   JfrProfiler$2(final JfrProfiler this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.summaryReporter = new SummaryReporter(() -> this.this$0.recording = null);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public void recordingStateChanged(final Recording rec) {
      if (rec == this.this$0.recording) {
         switch (rec.getState()) {
            case STOPPED:
               this.summaryReporter.recordingStopped(rec.getDestination());
               FlightRecorder.removeListener(this);
            case NEW:
            case DELAYED:
            case RUNNING:
            case CLOSED:
         }
      }
   }
}
