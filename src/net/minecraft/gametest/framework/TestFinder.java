package net.minecraft.gametest.framework;

import java.util.stream.Stream;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder$Reference;

public class TestFinder implements TestInstanceFinder, TestPosFinder {
   private static final TestInstanceFinder NO_FUNCTIONS = Stream::empty;
   private static final TestPosFinder NO_STRUCTURES = Stream::empty;
   private final TestInstanceFinder testInstanceFinder;
   private final TestPosFinder testPosFinder;
   private final CommandSourceStack source;

   @Override
   public Stream<GlobalPos> findTestPos() {
      return this.testPosFinder.findTestPos();
   }

   public static TestFinder$Builder builder() {
      return new TestFinder$Builder();
   }

   private TestFinder(final CommandSourceStack source, final TestInstanceFinder testInstanceFinder, final TestPosFinder testPosFinder) {
      this.source = source;
      this.testInstanceFinder = testInstanceFinder;
      this.testPosFinder = testPosFinder;
   }

   public CommandSourceStack source() {
      return this.source;
   }

   @Override
   public Stream<Holder$Reference<GameTestInstance>> findTests() {
      return this.testInstanceFinder.findTests();
   }
}
