package net.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.levelgen.WorldgenRandom;

@FunctionalInterface
public interface SinglePieceStructure$PieceConstructor {
   StructurePiece construct(WorldgenRandom random, int x, int z);
}
