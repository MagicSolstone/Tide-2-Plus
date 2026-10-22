package net.minecraft.gametest.framework;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

record TestCommand$TestBatchSummaryDisplayer(CommandSourceStack source) implements GameTestBatchListener {
   @Override
   public void testBatchStarting(final GameTestBatch batch) {
      this.source.sendSuccess(() -> Component.translatable("commands.test.batch.starting", batch.environment().getRegisteredName(), batch.index()), true);
   }

   @Override
   public void testBatchFinished(final GameTestBatch batch) {
   }
}
