package net.minecraft.world.level.chunk;

@FunctionalInterface
public interface CarvingMask$Visitor {
   void visitColumn(int x, int z, int bottomY, int topY);
}
