package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jspecify.annotations.Nullable;

public class NetherFortressPieces {
   private static final int MAX_DEPTH = 30;
   private static final int LOWEST_Y_POSITION = 10;
   public static final int MAGIC_START_Y = 64;
   private static final NetherFortressPieces$PieceWeight[] BRIDGE_PIECE_WEIGHTS = new NetherFortressPieces$PieceWeight[]{
      new NetherFortressPieces$PieceWeight(NetherFortressPieces$BridgeStraight.class, 30, 0, true),
      new NetherFortressPieces$PieceWeight(NetherFortressPieces$BridgeCrossing.class, 10, 4),
      new NetherFortressPieces$PieceWeight(NetherFortressPieces$RoomCrossing.class, 10, 4),
      new NetherFortressPieces$PieceWeight(NetherFortressPieces$StairsRoom.class, 10, 3),
      new NetherFortressPieces$PieceWeight(NetherFortressPieces$MonsterThrone.class, 5, 2),
      new NetherFortressPieces$PieceWeight(NetherFortressPieces$CastleEntrance.class, 5, 1)
   };
   private static final NetherFortressPieces$PieceWeight[] CASTLE_PIECE_WEIGHTS = new NetherFortressPieces$PieceWeight[]{
      new NetherFortressPieces$PieceWeight(NetherFortressPieces$CastleSmallCorridorPiece.class, 25, 0, true),
      new NetherFortressPieces$PieceWeight(NetherFortressPieces$CastleSmallCorridorCrossingPiece.class, 15, 5),
      new NetherFortressPieces$PieceWeight(NetherFortressPieces$CastleSmallCorridorRightTurnPiece.class, 5, 10),
      new NetherFortressPieces$PieceWeight(NetherFortressPieces$CastleSmallCorridorLeftTurnPiece.class, 5, 10),
      new NetherFortressPieces$PieceWeight(NetherFortressPieces$CastleCorridorStairsPiece.class, 10, 3, true),
      new NetherFortressPieces$PieceWeight(NetherFortressPieces$CastleCorridorTBalconyPiece.class, 7, 2),
      new NetherFortressPieces$PieceWeight(NetherFortressPieces$CastleStalkRoom.class, 5, 2)
   };

   @Nullable
   private static NetherFortressPieces$NetherBridgePiece findAndCreateBridgePieceFactory(
      final NetherFortressPieces$PieceWeight piece,
      final StructurePiecesBuilder builder,
      final RandomSource random,
      final int footX,
      final int footY,
      final int footZ,
      final Direction direction,
      final int depth
   ) {
      Class<? extends NetherFortressPieces$NetherBridgePiece> pieceClass = piece.pieceClass;
      NetherFortressPieces$NetherBridgePiece structurePiece = null;
      if (pieceClass == NetherFortressPieces$BridgeStraight.class) {
         structurePiece = NetherFortressPieces$BridgeStraight.createPiece(builder, random, footX, footY, footZ, direction, depth);
      } else if (pieceClass == NetherFortressPieces$BridgeCrossing.class) {
         structurePiece = NetherFortressPieces$BridgeCrossing.createPiece(builder, footX, footY, footZ, direction, depth);
      } else if (pieceClass == NetherFortressPieces$RoomCrossing.class) {
         structurePiece = NetherFortressPieces$RoomCrossing.createPiece(builder, footX, footY, footZ, direction, depth);
      } else if (pieceClass == NetherFortressPieces$StairsRoom.class) {
         structurePiece = NetherFortressPieces$StairsRoom.createPiece(builder, footX, footY, footZ, depth, direction);
      } else if (pieceClass == NetherFortressPieces$MonsterThrone.class) {
         structurePiece = NetherFortressPieces$MonsterThrone.createPiece(builder, footX, footY, footZ, depth, direction);
      } else if (pieceClass == NetherFortressPieces$CastleEntrance.class) {
         structurePiece = NetherFortressPieces$CastleEntrance.createPiece(builder, random, footX, footY, footZ, direction, depth);
      } else if (pieceClass == NetherFortressPieces$CastleSmallCorridorPiece.class) {
         structurePiece = NetherFortressPieces$CastleSmallCorridorPiece.createPiece(builder, footX, footY, footZ, direction, depth);
      } else if (pieceClass == NetherFortressPieces$CastleSmallCorridorRightTurnPiece.class) {
         structurePiece = NetherFortressPieces$CastleSmallCorridorRightTurnPiece.createPiece(builder, random, footX, footY, footZ, direction, depth);
      } else if (pieceClass == NetherFortressPieces$CastleSmallCorridorLeftTurnPiece.class) {
         structurePiece = NetherFortressPieces$CastleSmallCorridorLeftTurnPiece.createPiece(builder, random, footX, footY, footZ, direction, depth);
      } else if (pieceClass == NetherFortressPieces$CastleCorridorStairsPiece.class) {
         structurePiece = NetherFortressPieces$CastleCorridorStairsPiece.createPiece(builder, footX, footY, footZ, direction, depth);
      } else if (pieceClass == NetherFortressPieces$CastleCorridorTBalconyPiece.class) {
         structurePiece = NetherFortressPieces$CastleCorridorTBalconyPiece.createPiece(builder, footX, footY, footZ, direction, depth);
      } else if (pieceClass == NetherFortressPieces$CastleSmallCorridorCrossingPiece.class) {
         structurePiece = NetherFortressPieces$CastleSmallCorridorCrossingPiece.createPiece(builder, footX, footY, footZ, direction, depth);
      } else if (pieceClass == NetherFortressPieces$CastleStalkRoom.class) {
         structurePiece = NetherFortressPieces$CastleStalkRoom.createPiece(builder, footX, footY, footZ, direction, depth);
      }

      return structurePiece;
   }
}
