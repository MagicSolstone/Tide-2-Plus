package net.minecraft.world.level.chunk;

@FunctionalInterface
public interface CarvingMask$Filter {
   boolean test(int x, int y, int z);
}
