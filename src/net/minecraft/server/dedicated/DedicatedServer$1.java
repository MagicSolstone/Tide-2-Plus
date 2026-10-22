package net.minecraft.server.dedicated;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

class DedicatedServer$1 extends Thread {
   DedicatedServer$1(final DedicatedServer this$0, String name) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(name);
   }

   @Override
   public void run() {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

      String line;
      try {
         while (!this.this$0.isStopped() && this.this$0.isRunning() && (line = reader.readLine()) != null) {
            this.this$0.handleConsoleInput(line, this.this$0.createCommandSourceStack());
         }
      } catch (IOException var4) {
         DedicatedServer.LOGGER.error("Exception handling console input", var4);
      }
   }
}
