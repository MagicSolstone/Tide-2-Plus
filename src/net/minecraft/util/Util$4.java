package net.minecraft.util;

class Util$4 implements Runnable {
   Util$4(final Runnable val$task, final String val$name) {
      this.val$task = val$taskx;
      this.val$name = val$namex;
   }

   @Override
   public void run() {
      this.val$task.run();
   }

   @Override
   public String toString() {
      return this.val$name;
   }
}
