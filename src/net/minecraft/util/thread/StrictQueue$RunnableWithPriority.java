package net.minecraft.util.thread;

public record StrictQueue$RunnableWithPriority(int priority, Runnable task) implements Runnable {
   @Override
   public void run() {
      this.task.run();
   }
}
