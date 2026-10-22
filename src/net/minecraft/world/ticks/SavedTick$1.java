package net.minecraft.world.ticks;

import it.unimi.dsi.fastutil.Hash.Strategy;
import org.jspecify.annotations.Nullable;

class SavedTick$1 implements Strategy<SavedTick<?>> {
   public int hashCode(final SavedTick<?> o) {
      return 31 * o.pos().hashCode() + o.type().hashCode();
   }

   public boolean equals(@Nullable final SavedTick<?> a, @Nullable final SavedTick<?> b) {
      if (a == b) {
         return true;
      } else {
         return a != null && b != null ? a.type() == b.type() && a.pos().equals(b.pos()) : false;
      }
   }
}
