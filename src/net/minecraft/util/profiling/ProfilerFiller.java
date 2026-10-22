package net.minecraft.util.profiling;

import java.util.function.Supplier;
import net.minecraft.util.profiling.metrics.MetricCategory;

public interface ProfilerFiller {
   String ROOT = "root";

   void startTick();

   void endTick();

   void push(String name);

   void push(Supplier<String> name);

   void pop();

   void popPush(String name);

   void popPush(Supplier<String> name);

   default void addZoneText(final String text) {
   }

   default void addZoneValue(final long value) {
   }

   default void setZoneColor(final int color) {
   }

   default Zone zone(final String name) {
      this.push(name);
      return new Zone(this);
   }

   default Zone zone(final Supplier<String> name) {
      this.push(name);
      return new Zone(this);
   }

   void markForCharting(MetricCategory category);

   default void incrementCounter(final String name) {
      this.incrementCounter(name, 1);
   }

   void incrementCounter(String name, int amount);

   default void incrementCounter(final Supplier<String> name) {
      this.incrementCounter(name, 1);
   }

   void incrementCounter(Supplier<String> name, int amount);

   static ProfilerFiller combine(final ProfilerFiller first, final ProfilerFiller second) {
      if (first == InactiveProfiler.INSTANCE) {
         return second;
      } else {
         return (ProfilerFiller)(second == InactiveProfiler.INSTANCE ? first : new ProfilerFiller$CombinedProfileFiller(first, second));
      }
   }
}
