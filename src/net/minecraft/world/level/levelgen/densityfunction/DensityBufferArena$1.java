package net.minecraft.world.level.levelgen.densityfunction;

class DensityBufferArena$1 implements DensityBufferArena {
   @Override
   public ScopedDensityBuffer acquire(final int size) {
      return new ScopedDensityBuffer(this, size, size);
   }

   @Override
   public void release(final ScopedDensityBuffer buffer) {
   }
}
