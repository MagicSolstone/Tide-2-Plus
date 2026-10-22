package net.minecraft.world.level.levelgen.structure.structures;

class StrongholdPieces$PieceWeight {
   public final Class<? extends StrongholdPieces$StrongholdPiece> pieceClass;
   public final int weight;
   public int placeCount;
   public final int maxPlaceCount;

   public StrongholdPieces$PieceWeight(final Class<? extends StrongholdPieces$StrongholdPiece> pieceClass, final int weight, final int maxPlaceCount) {
      this.pieceClass = pieceClass;
      this.weight = weight;
      this.maxPlaceCount = maxPlaceCount;
   }

   public boolean doPlace(final int depth) {
      return this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount;
   }

   public boolean isValid() {
      return this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount;
   }
}
