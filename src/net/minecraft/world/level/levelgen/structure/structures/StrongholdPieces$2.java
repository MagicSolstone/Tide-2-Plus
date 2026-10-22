package net.minecraft.world.level.levelgen.structure.structures;

class StrongholdPieces$2 extends StrongholdPieces$PieceWeight {
   StrongholdPieces$2(final Class pieceClass, final int weight, final int maxPlaceCount) {
      super(pieceClass, weight, maxPlaceCount);
   }

   @Override
   public boolean doPlace(final int depth) {
      return super.doPlace(depth) && depth > 5;
   }
}
