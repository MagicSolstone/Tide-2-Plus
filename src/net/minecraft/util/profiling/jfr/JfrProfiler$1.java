package net.minecraft.util.profiling.jfr;

import java.util.Objects;
import jdk.jfr.FlightRecorderListener;
import jdk.jfr.Recording;

class JfrProfiler$1 implements FlightRecorderListener {
   JfrProfiler$1(final JfrProfiler this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public void recordingStateChanged(final Recording rec) {
      switch (rec.getState()) {
         case STOPPED:
            this.this$0.registerPeriodicEvents();
         case NEW:
         case DELAYED:
         case RUNNING:
         case CLOSED:
      }
   }
}
