package net.minecraft.world.level.levelgen.structure.pools;

import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableObject;

record JigsawPlacement$PieceState(PoolElementStructurePiece piece, MutableObject<VoxelShape> free, int depth) {
}
