package net.minecraft.util;

class Util$9 extends Thread {
   Util$9(String name) {
      super(name);
   }

   @Override
   public void run() {
      while (true) {
         try {
            Thread.sleep(2147483647L);
         } catch (InterruptedException var2) {
            Util.LOGGER.warn("Timer hack thread interrupted, that really should not happen");
            return;
         }
      }
   }
}
