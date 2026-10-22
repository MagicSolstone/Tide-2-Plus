package net.minecraft.world.level.levelgen.structure.structures;

class StrongholdPieces$1 extends StrongholdPieces$PieceWeight {
   StrongholdPieces$1(final Class pieceClass, final int weight, final int maxPlaceCount) {
      super(pieceClass, weight, maxPlaceCount);
   }

   @Override
   public boolean doPlace(final int depth) {
      return super.doPlace(depth) && depth > 4;
   }
}
