package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jspecify.annotations.Nullable;

public class MineshaftPieces {
   private static final int DEFAULT_SHAFT_WIDTH = 3;
   private static final int DEFAULT_SHAFT_HEIGHT = 3;
   private static final int DEFAULT_SHAFT_LENGTH = 5;
   private static final int MAX_PILLAR_HEIGHT = 20;
   private static final int MAX_CHAIN_HEIGHT = 50;
   private static final int MAX_DEPTH = 8;
   public static final int MAGIC_START_Y = 50;

   @Nullable
   private static MineshaftPieces$MineShaftPiece createRandomShaftPiece(
      final StructurePiecesBuilder builder,
      final RandomSource random,
      final int footX,
      final int footY,
      final int footZ,
      final Direction direction,
      final int genDepth,
      final MineshaftStructure$Type type
   ) {
      int randomSelection = random.nextInt(100);
      if (randomSelection >= 80) {
         BoundingBox crossingBox = MineshaftPieces$MineShaftCrossing.findCrossing(builder, random, footX, footY, footZ, direction);
         if (crossingBox != null) {
            return new MineshaftPieces$MineShaftCrossing(genDepth, crossingBox, direction, type);
         }
      } else if (randomSelection >= 70) {
         BoundingBox stairsBox = MineshaftPieces$MineShaftStairs.findStairs(builder, random, footX, footY, footZ, direction);
         if (stairsBox != null) {
            return new MineshaftPieces$MineShaftStairs(genDepth, stairsBox, direction, type);
         }
      } else {
         BoundingBox corridorBox = MineshaftPieces$MineShaftCorridor.findCorridorSize(builder, random, footX, footY, footZ, direction);
         if (corridorBox != null) {
            return new MineshaftPieces$MineShaftCorridor(genDepth, random, corridorBox, direction, type);
         }
      }

      return null;
   }

   @Nullable
   private static MineshaftPieces$MineShaftPiece generateAndAddPiece(
      final StructurePiece startPiece,
      final StructurePiecesBuilder builder,
      final RandomSource random,
      final int footX,
      final int footY,
      final int footZ,
      final Direction direction,
      final int depth
   ) {
      if (depth > 8) {
         return null;
      } else if (Math.abs(footX - startPiece.getBoundingBox().minX()) <= 80 && Math.abs(footZ - startPiece.getBoundingBox().minZ()) <= 80) {
         MineshaftStructure$Type type = ((MineshaftPieces$MineShaftPiece)startPiece).type;
         MineshaftPieces$MineShaftPiece newPiece = createRandomShaftPiece(builder, random, footX, footY, footZ, direction, depth + 1, type);
         if (newPiece != null) {
            builder.addPiece(newPiece);
            newPiece.addChildren(startPiece, builder, random);
         }

         return newPiece;
      } else {
         return null;
      }
   }
}
