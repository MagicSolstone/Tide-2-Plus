package net.minecraft.gametest.framework;

import java.util.Objects;
import java.util.function.Consumer;

class MultipleTestTracker$1 implements GameTestListener {
   MultipleTestTracker$1(final MultipleTestTracker this$0, final Consumer val$listener) {
      this.val$listener = val$listenerx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public void testStructureLoaded(final GameTestInfo testInfo) {
   }

   @Override
   public void testPassed(final GameTestInfo testInfo, final GameTestRunner runner) {
   }

   @Override
   public void testFailed(final GameTestInfo testInfo, final GameTestRunner runner) {
      this.val$listener.accept(testInfo);
   }

   @Override
   public void testAddedForRerun(final GameTestInfo original, final GameTestInfo copy, final GameTestRunner runner) {
   }
}
