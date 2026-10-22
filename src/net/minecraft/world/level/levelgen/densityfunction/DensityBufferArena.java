package net.minecraft.world.level.levelgen.densityfunction;

public interface DensityBufferArena {
   DensityBufferArena GLOBAL = new DensityBufferArena$1();

   ScopedDensityBuffer acquire(int size);

   void release(ScopedDensityBuffer buffer);
}
