package net.minecraft.world.level.levelgen.structure.structures;

class NetherFortressPieces$PieceWeight {
   public final Class<? extends NetherFortressPieces$NetherBridgePiece> pieceClass;
   public final int weight;
   public int placeCount;
   public final int maxPlaceCount;
   public final boolean allowInRow;

   public NetherFortressPieces$PieceWeight(
      final Class<? extends NetherFortressPieces$NetherBridgePiece> pieceClass, final int weight, final int maxPlaceCount, final boolean allowInRow
   ) {
      this.pieceClass = pieceClass;
      this.weight = weight;
      this.maxPlaceCount = maxPlaceCount;
      this.allowInRow = allowInRow;
   }

   public NetherFortressPieces$PieceWeight(final Class<? extends NetherFortressPieces$NetherBridgePiece> pieceClass, final int weight, final int maxPlaceCount) {
      this(pieceClass, weight, maxPlaceCount, false);
   }

   public boolean doPlace(final int depth) {
      return this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount;
   }

   public boolean isValid() {
      return this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount;
   }
}
