package net.minecraft.util;

import com.mojang.jtracy.TracyClient;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import org.jspecify.annotations.Nullable;

class Util$2 extends ForkJoinWorkerThread {
   Util$2(ForkJoinPool pool, final String val$threadName, final String val$name) {
      super(pool);
      this.val$threadName = val$threadNamex;
      this.val$name = val$namex;
   }

   @Override
   protected void onStart() {
      TracyClient.setThreadName(this.val$threadName, this.val$name.hashCode());
      super.onStart();
   }

   @Override
   protected void onTermination(@Nullable final Throwable exception) {
      if (exception != null) {
         Util.LOGGER.warn("{} died", this.getName(), exception);
      } else {
         Util.LOGGER.debug("{} shutdown", this.getName());
      }

      super.onTermination(exception);
   }
}
