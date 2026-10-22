package net.minecraft.world.ticks;

import net.minecraft.core.BlockPos;

class BlackholeTickAccess$2 implements LevelTickAccess<Object> {
   @Override
   public void schedule(final ScheduledTick<Object> tick) {
   }

   @Override
   public boolean hasScheduledTick(final BlockPos pos, final Object type) {
      return false;
   }

   @Override
   public boolean willTickThisTick(final BlockPos pos, final Object type) {
      return false;
   }

   @Override
   public int count() {
      return 0;
   }
}
