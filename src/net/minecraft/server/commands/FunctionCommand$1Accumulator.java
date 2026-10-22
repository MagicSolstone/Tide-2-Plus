package net.minecraft.server.commands;

class FunctionCommand$1Accumulator {
   private boolean anyResult;
   private int sum;

   public void add(final int result) {
      this.anyResult = true;
      this.sum += result;
   }
}
