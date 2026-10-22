package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public abstract class StrongholdPieces$Turn extends StrongholdPieces$StrongholdPiece {
   protected static final int WIDTH = 5;
   protected static final int HEIGHT = 5;
   protected static final int DEPTH = 5;

   protected StrongholdPieces$Turn(final StructurePieceType type, final int genDepth, final BoundingBox boundingBox) {
      super(type, genDepth, boundingBox);
   }

   public StrongholdPieces$Turn(final StructurePieceType type, final CompoundTag tag) {
      super(type, tag);
   }
}
