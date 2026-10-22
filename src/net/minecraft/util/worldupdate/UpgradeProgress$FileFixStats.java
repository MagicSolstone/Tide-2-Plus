package net.minecraft.util.worldupdate;

import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.util.Mth;

public class UpgradeProgress$FileFixStats {
   private final AtomicInteger finishedOperations = new AtomicInteger();
   private final AtomicInteger totalOperations = new AtomicInteger();

   public int finishedOperations() {
      return this.finishedOperations.get();
   }

   public int totalOperations() {
      return this.totalOperations.get();
   }

   public void reset() {
      this.finishedOperations.set(0);
      this.totalOperations.set(0);
   }

   public float getProgress() {
      return this.totalOperations() == 0 ? 0.0F : Mth.clamp((float)this.finishedOperations() / this.totalOperations(), 0.0F, 1.0F);
   }
}
