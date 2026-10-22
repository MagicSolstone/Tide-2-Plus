package net.minecraft.util;

import org.slf4j.Logger;

public class ProblemReporter$ScopedCollector extends ProblemReporter$Collector implements AutoCloseable {
   private final Logger logger;

   public ProblemReporter$ScopedCollector(final Logger logger) {
      this.logger = logger;
   }

   public ProblemReporter$ScopedCollector(final ProblemReporter$PathElement root, final Logger logger) {
      super(root);
      this.logger = logger;
   }

   @Override
   public void close() {
      if (!this.isEmpty()) {
         this.logger.warn("[{}] Serialization errors:\n{}", this.logger.getName(), this.getTreeReport());
      }
   }
}
