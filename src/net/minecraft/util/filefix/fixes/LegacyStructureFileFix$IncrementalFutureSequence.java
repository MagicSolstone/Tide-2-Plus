package net.minecraft.util.filefix.fixes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

class LegacyStructureFileFix$IncrementalFutureSequence {
   private final int maxConcurrency;
   private final List<CompletableFuture<?>> futures;

   public LegacyStructureFileFix$IncrementalFutureSequence(final int maxConcurrency) {
      this.maxConcurrency = maxConcurrency;
      this.futures = new ArrayList<>(maxConcurrency);
   }

   public int push(final CompletableFuture<?> future) {
      int finished = 0;
      if (this.futures.size() >= this.maxConcurrency) {
         finished += this.waitOnAny();
      }

      this.futures.add(future);
      return finished;
   }

   private int waitOnAny() {
      int oldSize = this.futures.size();
      CompletableFuture.anyOf(this.futures.toArray(CompletableFuture[]::new)).join();
      this.futures.removeIf(CompletableFuture::isDone);
      return oldSize - this.futures.size();
   }

   public int waitForAll() {
      int oldSize = this.futures.size();
      CompletableFuture.allOf(this.futures.toArray(CompletableFuture[]::new)).join();
      this.futures.clear();
      return oldSize;
   }
}
