package net.minecraft.util.profiling;

import java.util.function.Supplier;
import net.minecraft.util.profiling.metrics.MetricCategory;

public class ProfilerFiller$CombinedProfileFiller implements ProfilerFiller {
   private final ProfilerFiller first;
   private final ProfilerFiller second;

   public ProfilerFiller$CombinedProfileFiller(final ProfilerFiller first, final ProfilerFiller second) {
      this.first = first;
      this.second = second;
   }

   @Override
   public void startTick() {
      this.first.startTick();
      this.second.startTick();
   }

   @Override
   public void endTick() {
      this.first.endTick();
      this.second.endTick();
   }

   @Override
   public void push(final String name) {
      this.first.push(name);
      this.second.push(name);
   }

   @Override
   public void push(final Supplier<String> name) {
      this.first.push(name);
      this.second.push(name);
   }

   @Override
   public void markForCharting(final MetricCategory category) {
      this.first.markForCharting(category);
      this.second.markForCharting(category);
   }

   @Override
   public void pop() {
      this.first.pop();
      this.second.pop();
   }

   @Override
   public void popPush(final String name) {
      this.first.popPush(name);
      this.second.popPush(name);
   }

   @Override
   public void popPush(final Supplier<String> name) {
      this.first.popPush(name);
      this.second.popPush(name);
   }

   @Override
   public void incrementCounter(final String name, final int amount) {
      this.first.incrementCounter(name, amount);
      this.second.incrementCounter(name, amount);
   }

   @Override
   public void incrementCounter(final Supplier<String> name, final int amount) {
      this.first.incrementCounter(name, amount);
      this.second.incrementCounter(name, amount);
   }

   @Override
   public void addZoneText(final String text) {
      this.first.addZoneText(text);
      this.second.addZoneText(text);
   }

   @Override
   public void addZoneValue(final long value) {
      this.first.addZoneValue(value);
      this.second.addZoneValue(value);
   }

   @Override
   public void setZoneColor(final int color) {
      this.first.setZoneColor(color);
      this.second.setZoneColor(color);
   }
}
