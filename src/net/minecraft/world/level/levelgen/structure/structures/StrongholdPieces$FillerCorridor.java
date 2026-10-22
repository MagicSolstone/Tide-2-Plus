package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jspecify.annotations.Nullable;

public class StrongholdPieces$FillerCorridor extends StrongholdPieces$StrongholdPiece {
   private final int steps;

   public StrongholdPieces$FillerCorridor(final int genDepth, final BoundingBox boundingBox, final Direction direction) {
      super(StructurePieceType.STRONGHOLD_FILLER_CORRIDOR, genDepth, boundingBox);
      this.setOrientation(direction);
      this.steps = direction != Direction.NORTH && direction != Direction.SOUTH ? boundingBox.getXSpan() : boundingBox.getZSpan();
   }

   public StrongholdPieces$FillerCorridor(final CompoundTag tag) {
      super(StructurePieceType.STRONGHOLD_FILLER_CORRIDOR, tag);
      this.steps = tag.getIntOr("Steps", 0);
   }

   @Override
   protected void addAdditionalSaveData(final StructurePieceSerializationContext context, final CompoundTag tag) {
      super.addAdditionalSaveData(context, tag);
      tag.putInt("Steps", this.steps);
   }

   @Nullable
   public static BoundingBox findPieceBox(
      final StructurePiecesBuilder builder, final RandomSource random, final int footX, final int footY, final int footZ, final Direction direction
   ) {
      int maxLength = 3;
      BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, 4, direction);
      StructurePiece collisionPiece = builder.findCollisionPiece(box);
      if (collisionPiece == null) {
         return null;
      } else {
         if (collisionPiece.getBoundingBox().minY() == box.minY()) {
            for (int depth = 2; depth >= 1; depth--) {
               box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, depth, direction);
               if (!collisionPiece.getBoundingBox().intersects(box)) {
                  return BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, depth + 1, direction);
               }
            }
         }

         return null;
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
      for (int i = 0; i < this.steps; i++) {
         this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 0, 0, i, chunkBB);
         this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 0, i, chunkBB);
         this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 0, i, chunkBB);
         this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 0, i, chunkBB);
         this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 4, 0, i, chunkBB);

         for (int y = 1; y <= 3; y++) {
            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 0, y, i, chunkBB);
            this.placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), 1, y, i, chunkBB);
            this.placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), 2, y, i, chunkBB);
            this.placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), 3, y, i, chunkBB);
            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 4, y, i, chunkBB);
         }

         this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 0, 4, i, chunkBB);
         this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 4, i, chunkBB);
         this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 4, i, chunkBB);
         this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 4, i, chunkBB);
         this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 4, 4, i, chunkBB);
      }
   }
}
