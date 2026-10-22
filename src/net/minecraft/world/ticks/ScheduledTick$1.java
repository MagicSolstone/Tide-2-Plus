package net.minecraft.world.ticks;

import it.unimi.dsi.fastutil.Hash.Strategy;
import org.jspecify.annotations.Nullable;

class ScheduledTick$1 implements Strategy<ScheduledTick<?>> {
   public int hashCode(final ScheduledTick<?> o) {
      return 31 * o.pos().hashCode() + o.type().hashCode();
   }

   public boolean equals(@Nullable final ScheduledTick<?> a, @Nullable final ScheduledTick<?> b) {
      if (a == b) {
         return true;
      } else {
         return a != null && b != null ? a.type() == b.type() && a.pos().equals(b.pos()) : false;
      }
   }
}
