package net.minecraft.world.level.levelgen.structure.structures;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jspecify.annotations.Nullable;

public class StrongholdPieces {
   private static final int SMALL_DOOR_WIDTH = 3;
   private static final int SMALL_DOOR_HEIGHT = 3;
   private static final int MAX_DEPTH = 50;
   private static final int LOWEST_Y_POSITION = 10;
   private static final boolean CHECK_AIR = true;
   public static final int MAGIC_START_Y = 64;
   private static final StrongholdPieces$PieceWeight[] STRONGHOLD_PIECE_WEIGHTS = new StrongholdPieces$PieceWeight[]{
      new StrongholdPieces$PieceWeight(StrongholdPieces$Straight.class, 40, 0),
      new StrongholdPieces$PieceWeight(StrongholdPieces$PrisonHall.class, 5, 5),
      new StrongholdPieces$PieceWeight(StrongholdPieces$LeftTurn.class, 20, 0),
      new StrongholdPieces$PieceWeight(StrongholdPieces$RightTurn.class, 20, 0),
      new StrongholdPieces$PieceWeight(StrongholdPieces$RoomCrossing.class, 10, 6),
      new StrongholdPieces$PieceWeight(StrongholdPieces$StraightStairsDown.class, 5, 5),
      new StrongholdPieces$PieceWeight(StrongholdPieces$StairsDown.class, 5, 5),
      new StrongholdPieces$PieceWeight(StrongholdPieces$FiveCrossing.class, 5, 4),
      new StrongholdPieces$PieceWeight(StrongholdPieces$ChestCorridor.class, 5, 4),
      new StrongholdPieces$1(StrongholdPieces$Library.class, 10, 2),
      new StrongholdPieces$2(StrongholdPieces$PortalRoom.class, 20, 1)
   };
   private static List<StrongholdPieces$PieceWeight> currentPieces;
   @Nullable
   private static Class<? extends StrongholdPieces$StrongholdPiece> imposedPiece;
   private static int totalWeight;
   private static final StrongholdPieces$SmoothStoneSelector SMOOTH_STONE_SELECTOR = new StrongholdPieces$SmoothStoneSelector();

   public static void resetPieces() {
      currentPieces = Lists.newArrayList();

      for (StrongholdPieces$PieceWeight piece : STRONGHOLD_PIECE_WEIGHTS) {
         piece.placeCount = 0;
         currentPieces.add(piece);
      }

      imposedPiece = null;
   }

   private static boolean updatePieceWeight() {
      boolean hasAnyPieces = false;
      totalWeight = 0;

      for (StrongholdPieces$PieceWeight piece : currentPieces) {
         if (piece.maxPlaceCount > 0 && piece.placeCount < piece.maxPlaceCount) {
            hasAnyPieces = true;
         }

         totalWeight = totalWeight + piece.weight;
      }

      return hasAnyPieces;
   }

   @Nullable
   private static StrongholdPieces$StrongholdPiece findAndCreatePieceFactory(
      final Class<? extends StrongholdPieces$StrongholdPiece> pieceClass,
      final StructurePiecesBuilder builder,
      final RandomSource random,
      final int footX,
      final int footY,
      final int footZ,
      final Direction direction,
      final int depth
   ) {
      StrongholdPieces$StrongholdPiece strongholdPiece = null;
      if (pieceClass == StrongholdPieces$Straight.class) {
         strongholdPiece = StrongholdPieces$Straight.createPiece(builder, random, footX, footY, footZ, direction, depth);
      } else if (pieceClass == StrongholdPieces$PrisonHall.class) {
         strongholdPiece = StrongholdPieces$PrisonHall.createPiece(builder, random, footX, footY, footZ, direction, depth);
      } else if (pieceClass == StrongholdPieces$LeftTurn.class) {
         strongholdPiece = StrongholdPieces$LeftTurn.createPiece(builder, random, footX, footY, footZ, direction, depth);
      } else if (pieceClass == StrongholdPieces$RightTurn.class) {
         strongholdPiece = StrongholdPieces$RightTurn.createPiece(builder, random, footX, footY, footZ, direction, depth);
      } else if (pieceClass == StrongholdPieces$RoomCrossing.class) {
         strongholdPiece = StrongholdPieces$RoomCrossing.createPiece(builder, random, footX, footY, footZ, direction, depth);
      } else if (pieceClass == StrongholdPieces$StraightStairsDown.class) {
         strongholdPiece = StrongholdPieces$StraightStairsDown.createPiece(builder, random, footX, footY, footZ, direction, depth);
      } else if (pieceClass == StrongholdPieces$StairsDown.class) {
         strongholdPiece = StrongholdPieces$StairsDown.createPiece(builder, random, footX, footY, footZ, direction, depth);
      } else if (pieceClass == StrongholdPieces$FiveCrossing.class) {
         strongholdPiece = StrongholdPieces$FiveCrossing.createPiece(builder, random, footX, footY, footZ, direction, depth);
      } else if (pieceClass == StrongholdPieces$ChestCorridor.class) {
         strongholdPiece = StrongholdPieces$ChestCorridor.createPiece(builder, random, footX, footY, footZ, direction, depth);
      } else if (pieceClass == StrongholdPieces$Library.class) {
         strongholdPiece = StrongholdPieces$Library.createPiece(builder, random, footX, footY, footZ, direction, depth);
      } else if (pieceClass == StrongholdPieces$PortalRoom.class) {
         strongholdPiece = StrongholdPieces$PortalRoom.createPiece(builder, footX, footY, footZ, direction, depth);
      }

      return strongholdPiece;
   }

   @Nullable
   private static StrongholdPieces$StrongholdPiece generatePieceFromSmallDoor(
      final StrongholdPieces$StartPiece startPiece,
      final StructurePiecesBuilder builder,
      final RandomSource random,
      final int footX,
      final int footY,
      final int footZ,
      final Direction direction,
      final int depth
   ) {
      if (!updatePieceWeight()) {
         return null;
      } else {
         if (imposedPiece != null) {
            StrongholdPieces$StrongholdPiece strongholdPiece = findAndCreatePieceFactory(imposedPiece, builder, random, footX, footY, footZ, direction, depth);
            imposedPiece = null;
            if (strongholdPiece != null) {
               return strongholdPiece;
            }
         }

         int numAttempts = 0;

         while (numAttempts < 5) {
            numAttempts++;
            int weightSelection = random.nextInt(totalWeight);

            for (StrongholdPieces$PieceWeight piece : currentPieces) {
               weightSelection -= piece.weight;
               if (weightSelection < 0) {
                  if (!piece.doPlace(depth) || piece == startPiece.previousPiece) {
                     break;
                  }

                  StrongholdPieces$StrongholdPiece strongholdPiece = findAndCreatePieceFactory(
                     piece.pieceClass, builder, random, footX, footY, footZ, direction, depth
                  );
                  if (strongholdPiece != null) {
                     piece.placeCount++;
                     startPiece.previousPiece = piece;
                     if (!piece.isValid()) {
                        currentPieces.remove(piece);
                     }

                     return strongholdPiece;
                  }
               }
            }
         }

         BoundingBox box = StrongholdPieces$FillerCorridor.findPieceBox(builder, random, footX, footY, footZ, direction);
         return box != null && box.minY() > 1 ? new StrongholdPieces$FillerCorridor(depth, box, direction) : null;
      }
   }

   @Nullable
   private static StructurePiece generateAndAddPiece(
      final StrongholdPieces$StartPiece startPiece,
      final StructurePiecesBuilder builder,
      final RandomSource random,
      final int footX,
      final int footY,
      final int footZ,
      final Direction direction,
      final int depth
   ) {
      if (depth > 50) {
         return null;
      } else if (Math.abs(footX - startPiece.getBoundingBox().minX()) <= 112 && Math.abs(footZ - startPiece.getBoundingBox().minZ()) <= 112) {
         StructurePiece newPiece = generatePieceFromSmallDoor(startPiece, builder, random, footX, footY, footZ, direction, depth + 1);
         if (newPiece != null) {
            builder.addPiece(newPiece);
            startPiece.pendingChildren.add(newPiece);
         }

         return newPiece;
      } else {
         return null;
      }
   }
}
