package net.minecraft.gametest.framework;

import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public final class TestCommand$TestSummaryDisplayer implements GameTestListener {
   private final CommandSourceStack source;
   private final MultipleTestTracker tracker;
   private final Set<ResourceKey<Level>> dimensions = new ReferenceArraySet();

   public TestCommand$TestSummaryDisplayer(final CommandSourceStack source, final MultipleTestTracker tracker) {
      this.source = source;
      this.tracker = tracker;
   }

   @Override
   public void testStructureLoaded(final GameTestInfo testInfo) {
   }

   @Override
   public void testPassed(final GameTestInfo testInfo, final GameTestRunner runner) {
      this.dimensions.add(testInfo.getTest().dimension());
      this.showTestSummaryIfAllDone();
   }

   @Override
   public void testFailed(final GameTestInfo testInfo, final GameTestRunner runner) {
      this.dimensions.add(testInfo.getTest().dimension());
      this.showTestSummaryIfAllDone();
   }

   @Override
   public void testAddedForRerun(final GameTestInfo original, final GameTestInfo copy, final GameTestRunner runner) {
      this.tracker.addTestToTrack(copy);
   }

   private void showTestSummaryIfAllDone() {
      if (this.tracker.isDone()) {
         this.source.sendSuccess(() -> Component.translatable("commands.test.summary", this.tracker.getTotalCount()).withStyle(ChatFormatting.WHITE), true);
         if (this.tracker.hasFailedRequired()) {
            this.source.sendFailure(Component.translatable("commands.test.summary.failed", this.tracker.getFailedRequiredCount()));
         } else {
            this.source.sendSuccess(() -> Component.translatable("commands.test.summary.all_required_passed").withStyle(ChatFormatting.GREEN), true);
         }

         if (this.tracker.hasFailedOptional()) {
            this.source.sendSystemMessage(Component.translatable("commands.test.summary.optional_failed", this.tracker.getFailedOptionalCount()));
         }

         TestCommand.outputPlayerCoordinates(this.source, this.dimensions);
         TestCommand.outputTestCoordinates(this.source, this.dimensions);
      }
   }
}
