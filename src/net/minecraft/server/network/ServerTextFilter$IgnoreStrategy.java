package net.minecraft.server.network;

@FunctionalInterface
public interface ServerTextFilter$IgnoreStrategy {
   ServerTextFilter$IgnoreStrategy NEVER_IGNORE = (message, removedCharCount) -> false;
   ServerTextFilter$IgnoreStrategy IGNORE_FULLY_FILTERED = (message, removedCharCount) -> message.length() == removedCharCount;

   static ServerTextFilter$IgnoreStrategy ignoreOverThreshold(final int threshold) {
      return (message, removedCharCount) -> removedCharCount >= threshold;
   }

   static ServerTextFilter$IgnoreStrategy select(final int hashesToDrop) {
      return switch (hashesToDrop) {
         case -1 -> NEVER_IGNORE;
         case 0 -> IGNORE_FULLY_FILTERED;
         default -> ignoreOverThreshold(hashesToDrop);
      };
   }

   boolean shouldIgnore(final String message, final int removedCharCount);
}
