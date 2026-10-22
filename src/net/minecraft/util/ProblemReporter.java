package net.minecraft.util;

public interface ProblemReporter {
   ProblemReporter DISCARDING = new ProblemReporter$1();

   ProblemReporter forChild(ProblemReporter$PathElement path);

   void report(ProblemReporter$Problem problem);
}
