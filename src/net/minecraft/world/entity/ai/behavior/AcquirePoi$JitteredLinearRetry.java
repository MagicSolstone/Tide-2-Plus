package net.minecraft.world.entity.ai.behavior;

import net.minecraft.util.RandomSource;

class AcquirePoi$JitteredLinearRetry {
   private static final int MIN_INTERVAL_INCREASE = 40;
   private static final int MAX_INTERVAL_INCREASE = 80;
   private static final int MAX_RETRY_PATHFINDING_INTERVAL = 400;
   private final RandomSource random;
   private long previousAttemptTimestamp;
   private long nextScheduledAttemptTimestamp;
   private int currentDelay;

   public AcquirePoi$JitteredLinearRetry(final RandomSource random, final long firstAttemptTimestamp) {
      this.random = random;
      this.markAttempt(firstAttemptTimestamp);
   }

   public void markAttempt(final long timestamp) {
      this.previousAttemptTimestamp = timestamp;
      int suggestedDelay = this.currentDelay + this.random.nextInt(40) + 40;
      this.currentDelay = Math.min(suggestedDelay, 400);
      this.nextScheduledAttemptTimestamp = timestamp + this.currentDelay;
   }

   public boolean isStillValid(final long timestamp) {
      return timestamp - this.previousAttemptTimestamp < 400L;
   }

   public boolean shouldRetry(final long timestamp) {
      return timestamp >= this.nextScheduledAttemptTimestamp;
   }

   @Override
   public String toString() {
      return "RetryMarker{, previousAttemptAt="
         + this.previousAttemptTimestamp
         + ", nextScheduledAttemptAt="
         + this.nextScheduledAttemptTimestamp
         + ", currentDelay="
         + this.currentDelay
         + "}";
   }
}
