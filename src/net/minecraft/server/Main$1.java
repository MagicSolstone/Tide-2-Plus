package net.minecraft.server;

import net.minecraft.server.dedicated.DedicatedServer;

class Main$1 extends Thread {
   Main$1(String name, final DedicatedServer val$dedicatedServer) {
      super(name);
      this.val$dedicatedServer = val$dedicatedServerx;
   }

   @Override
   public void run() {
      this.val$dedicatedServer.halt(true);
   }
}
