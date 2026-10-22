package net.minecraft.server.dedicated;

import java.util.Objects;
import java.util.TimerTask;

class ServerWatchdog$1 extends TimerTask {
   ServerWatchdog$1(final ServerWatchdog this$0) {
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public void run() {
      Runtime.getRuntime().halt(1);
   }
}
