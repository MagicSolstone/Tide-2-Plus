package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jspecify.annotations.Nullable;

public class MineshaftPieces$MineShaftStairs extends MineshaftPieces$MineShaftPiece {
   public MineshaftPieces$MineShaftStairs(final int genDepth, final BoundingBox boundingBox, final Direction direction, final MineshaftStructure$Type type) {
      super(StructurePieceType.MINE_SHAFT_STAIRS, genDepth, type, boundingBox);
      this.setOrientation(direction);
   }

   public MineshaftPieces$MineShaftStairs(final CompoundTag tag) {
      super(StructurePieceType.MINE_SHAFT_STAIRS, tag);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Nullable
   public static BoundingBox findStairs(
      final StructurePiecesBuilder builder, final RandomSource random, final int footX, final int footY, final int footZ, final Direction direction
   ) {
      BoundingBox box = switch (direction) {
         default -> new BoundingBox(0, -5, -8, 2, 2, 0);
         case SOUTH -> new BoundingBox(0, -5, 0, 2, 2, 8);
         case WEST -> new BoundingBox(-8, -5, 0, 0, 2, 2);
         case EAST -> new BoundingBox(0, -5, 0, 8, 2, 2);
      };
      box.move(footX, footY, footZ);
      return builder.findCollisionPiece(box) != null ? null : box;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public void addChildren(final StructurePiece startPiece, final StructurePiecesBuilder builder, final RandomSource random) {
      int depth = this.getGenDepth();
      Direction orientation = this.getOrientation();
      if (orientation != null) {
         switch (orientation) {
            case NORTH:
            default:
               MineshaftPieces.generateAndAddPiece(
                  startPiece, builder, random, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, depth
               );
               break;
            case SOUTH:
               MineshaftPieces.generateAndAddPiece(
                  startPiece, builder, random, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, depth
               );
               break;
            case WEST:
               MineshaftPieces.generateAndAddPiece(
                  startPiece, builder, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ(), Direction.WEST, depth
               );
               break;
            case EAST:
               MineshaftPieces.generateAndAddPiece(
                  startPiece, builder, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ(), Direction.EAST, depth
               );
         }
      }
   }

   @Override
   public void postProcess(
      final WorldGenLevel level,
      final StructureManager structureManager,
      final ChunkGenerator generator,
      final RandomSource random,
      final BoundingBox chunkBB,
      final ChunkPos chunkPos,
      final BlockPos referencePos
   ) {
      if (!this.isInInvalidLocation(level, chunkBB)) {
         this.generateBox(level, chunkBB, 0, 5, 0, 2, 7, 1, CAVE_AIR, CAVE_AIR, false);
         this.generateBox(level, chunkBB, 0, 0, 7, 2, 2, 8, CAVE_AIR, CAVE_AIR, false);

         for (int i = 0; i < 5; i++) {
            this.generateBox(level, chunkBB, 0, 5 - i - (i < 4 ? 1 : 0), 2 + i, 2, 7 - i, 2 + i, CAVE_AIR, CAVE_AIR, false);
         }
      }
   }
}
