package net.minecraft.world.level.levelgen.structure.structures;

import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jspecify.annotations.Nullable;

abstract class NetherFortressPieces$NetherBridgePiece extends StructurePiece {
   protected NetherFortressPieces$NetherBridgePiece(final StructurePieceType type, final int genDepth, final BoundingBox boundingBox) {
      super(type, genDepth, boundingBox);
   }

   public NetherFortressPieces$NetherBridgePiece(final StructurePieceType type, final CompoundTag tag) {
      super(type, tag);
   }

   @Override
   protected void addAdditionalSaveData(final StructurePieceSerializationContext context, final CompoundTag tag) {
   }

   private int updatePieceWeight(final List<NetherFortressPieces$PieceWeight> currentPieces) {
      boolean hasAnyPieces = false;
      int totalWeight = 0;

      for (NetherFortressPieces$PieceWeight piece : currentPieces) {
         if (piece.maxPlaceCount > 0 && piece.placeCount < piece.maxPlaceCount) {
            hasAnyPieces = true;
         }

         totalWeight += piece.weight;
      }

      return hasAnyPieces ? totalWeight : -1;
   }

   @Nullable
   private NetherFortressPieces$NetherBridgePiece generatePiece(
      final NetherFortressPieces$StartPiece startPiece,
      final List<NetherFortressPieces$PieceWeight> currentPieces,
      final StructurePiecesBuilder builder,
      final RandomSource random,
      final int footX,
      final int footY,
      final int footZ,
      final Direction direction,
      final int depth
   ) {
      int totalWeight = this.updatePieceWeight(currentPieces);
      boolean doStuff = totalWeight > 0 && depth <= 30;
      int numAttempts = 0;

      while (numAttempts < 5 && doStuff) {
         numAttempts++;
         int weightSelection = random.nextInt(totalWeight);

         for (NetherFortressPieces$PieceWeight piece : currentPieces) {
            weightSelection -= piece.weight;
            if (weightSelection < 0) {
               if (!piece.doPlace(depth) || piece == startPiece.previousPiece && !piece.allowInRow) {
                  break;
               }

               NetherFortressPieces$NetherBridgePiece structurePiece = NetherFortressPieces.findAndCreateBridgePieceFactory(
                  piece, builder, random, footX, footY, footZ, direction, depth
               );
               if (structurePiece != null) {
                  piece.placeCount++;
                  startPiece.previousPiece = piece;
                  if (!piece.isValid()) {
                     currentPieces.remove(piece);
                  }

                  return structurePiece;
               }
            }
         }
      }

      return NetherFortressPieces$BridgeEndFiller.createPiece(builder, random, footX, footY, footZ, direction, depth);
   }

   @Nullable
   private StructurePiece generateAndAddPiece(
      final NetherFortressPieces$StartPiece startPiece,
      final StructurePiecesBuilder builder,
      final RandomSource random,
      final int footX,
      final int footY,
      final int footZ,
      final Direction direction,
      final int depth,
      final boolean isCastle
   ) {
      if (Math.abs(footX - startPiece.getBoundingBox().minX()) <= 112 && Math.abs(footZ - startPiece.getBoundingBox().minZ()) <= 112) {
         List<NetherFortressPieces$PieceWeight> availablePieces = startPiece.availableBridgePieces;
         if (isCastle) {
            availablePieces = startPiece.availableCastlePieces;
         }

         StructurePiece newPiece = this.generatePiece(startPiece, availablePieces, builder, random, footX, footY, footZ, direction, depth + 1);
         if (newPiece != null) {
            builder.addPiece(newPiece);
            startPiece.pendingChildren.add(newPiece);
         }

         return newPiece;
      } else {
         return NetherFortressPieces$BridgeEndFiller.createPiece(builder, random, footX, footY, footZ, direction, depth);
      }
   }

   @Nullable
   protected StructurePiece generateChildForward(
      final NetherFortressPieces$StartPiece startPiece,
      final StructurePiecesBuilder builder,
      final RandomSource random,
      final int xOff,
      final int yOff,
      final boolean isCastle
   ) {
      Direction orientation = this.getOrientation();
      if (orientation != null) {
         switch (orientation) {
            case NORTH:
               return this.generateAndAddPiece(
                  startPiece,
                  builder,
                  random,
                  this.boundingBox.minX() + xOff,
                  this.boundingBox.minY() + yOff,
                  this.boundingBox.minZ() - 1,
                  orientation,
                  this.getGenDepth(),
                  isCastle
               );
            case SOUTH:
               return this.generateAndAddPiece(
                  startPiece,
                  builder,
                  random,
                  this.boundingBox.minX() + xOff,
                  this.boundingBox.minY() + yOff,
                  this.boundingBox.maxZ() + 1,
                  orientation,
                  this.getGenDepth(),
                  isCastle
               );
            case WEST:
               return this.generateAndAddPiece(
                  startPiece,
                  builder,
                  random,
                  this.boundingBox.minX() - 1,
                  this.boundingBox.minY() + yOff,
                  this.boundingBox.minZ() + xOff,
                  orientation,
                  this.getGenDepth(),
                  isCastle
               );
            case EAST:
               return this.generateAndAddPiece(
                  startPiece,
                  builder,
                  random,
                  this.boundingBox.maxX() + 1,
                  this.boundingBox.minY() + yOff,
                  this.boundingBox.minZ() + xOff,
                  orientation,
                  this.getGenDepth(),
                  isCastle
               );
         }
      }

      return null;
   }

   @Nullable
   protected StructurePiece generateChildLeft(
      final NetherFortressPieces$StartPiece startPiece,
      final StructurePiecesBuilder builder,
      final RandomSource random,
      final int yOff,
      final int zOff,
      final boolean isCastle
   ) {
      Direction orientation = this.getOrientation();
      if (orientation != null) {
         switch (orientation) {
            case NORTH:
               return this.generateAndAddPiece(
                  startPiece,
                  builder,
                  random,
                  this.boundingBox.minX() - 1,
                  this.boundingBox.minY() + yOff,
                  this.boundingBox.minZ() + zOff,
                  Direction.WEST,
                  this.getGenDepth(),
                  isCastle
               );
            case SOUTH:
               return this.generateAndAddPiece(
                  startPiece,
                  builder,
                  random,
                  this.boundingBox.minX() - 1,
                  this.boundingBox.minY() + yOff,
                  this.boundingBox.minZ() + zOff,
                  Direction.WEST,
                  this.getGenDepth(),
                  isCastle
               );
            case WEST:
               return this.generateAndAddPiece(
                  startPiece,
                  builder,
                  random,
                  this.boundingBox.minX() + zOff,
                  this.boundingBox.minY() + yOff,
                  this.boundingBox.minZ() - 1,
                  Direction.NORTH,
                  this.getGenDepth(),
                  isCastle
               );
            case EAST:
               return this.generateAndAddPiece(
                  startPiece,
                  builder,
                  random,
                  this.boundingBox.minX() + zOff,
                  this.boundingBox.minY() + yOff,
                  this.boundingBox.minZ() - 1,
                  Direction.NORTH,
                  this.getGenDepth(),
                  isCastle
               );
         }
      }

      return null;
   }

   @Nullable
   protected StructurePiece generateChildRight(
      final NetherFortressPieces$StartPiece startPiece,
      final StructurePiecesBuilder builder,
      final RandomSource random,
      final int yOff,
      final int zOff,
      final boolean isCastle
   ) {
      Direction orientation = this.getOrientation();
      if (orientation != null) {
         switch (orientation) {
            case NORTH:
               return this.generateAndAddPiece(
                  startPiece,
                  builder,
                  random,
                  this.boundingBox.maxX() + 1,
                  this.boundingBox.minY() + yOff,
                  this.boundingBox.minZ() + zOff,
                  Direction.EAST,
                  this.getGenDepth(),
                  isCastle
               );
            case SOUTH:
               return this.generateAndAddPiece(
                  startPiece,
                  builder,
                  random,
                  this.boundingBox.maxX() + 1,
                  this.boundingBox.minY() + yOff,
                  this.boundingBox.minZ() + zOff,
                  Direction.EAST,
                  this.getGenDepth(),
                  isCastle
               );
            case WEST:
               return this.generateAndAddPiece(
                  startPiece,
                  builder,
                  random,
                  this.boundingBox.minX() + zOff,
                  this.boundingBox.minY() + yOff,
                  this.boundingBox.maxZ() + 1,
                  Direction.SOUTH,
                  this.getGenDepth(),
                  isCastle
               );
            case EAST:
               return this.generateAndAddPiece(
                  startPiece,
                  builder,
                  random,
                  this.boundingBox.minX() + zOff,
                  this.boundingBox.minY() + yOff,
                  this.boundingBox.maxZ() + 1,
                  Direction.SOUTH,
                  this.getGenDepth(),
                  isCastle
               );
         }
      }

      return null;
   }

   protected static boolean isOkBox(final BoundingBox box) {
      return box.minY() > 10;
   }
}
