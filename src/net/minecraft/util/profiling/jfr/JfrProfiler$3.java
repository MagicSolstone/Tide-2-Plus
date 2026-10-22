package net.minecraft.util.profiling.jfr;

import jdk.jfr.RecordingState;

// $VF: synthetic class
class JfrProfiler$3 {
   static {
      try {
         $SwitchMap$jdk$jfr$RecordingState[RecordingState.STOPPED.ordinal()] = 1;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$jdk$jfr$RecordingState[RecordingState.NEW.ordinal()] = 2;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$jdk$jfr$RecordingState[RecordingState.DELAYED.ordinal()] = 3;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$jdk$jfr$RecordingState[RecordingState.RUNNING.ordinal()] = 4;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$jdk$jfr$RecordingState[RecordingState.CLOSED.ordinal()] = 5;
      } catch (NoSuchFieldError var1) {
      }
   }
}
