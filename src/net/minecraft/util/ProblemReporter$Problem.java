package net.minecraft.util;

public interface ProblemReporter$Problem {
   String description();

   default boolean isFatal() {
      return false;
   }
}
