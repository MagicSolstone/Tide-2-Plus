package net.minecraft.world.ticks;

@FunctionalInterface
interface LevelTicks$PosAndContainerConsumer<T> {
   void accept(long pos, LevelChunkTicks<T> container);
}
