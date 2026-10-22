package net.minecraft.world.ticks;

import net.minecraft.core.BlockPos;

class BlackholeTickAccess$1 implements TickContainerAccess<Object> {
   @Override
   public void schedule(final ScheduledTick<Object> tick) {
   }

   @Override
   public boolean hasScheduledTick(final BlockPos pos, final Object type) {
      return false;
   }

   @Override
   public int count() {
      return 0;
   }
}
