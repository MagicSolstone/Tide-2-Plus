package net.minecraft.util;

class ProblemReporter$1 implements ProblemReporter {
   @Override
   public ProblemReporter forChild(final ProblemReporter$PathElement path) {
      return this;
   }

   @Override
   public void report(final ProblemReporter$Problem problem) {
   }
}
