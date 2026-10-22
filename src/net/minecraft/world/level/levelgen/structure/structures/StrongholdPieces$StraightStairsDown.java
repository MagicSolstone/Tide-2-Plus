package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jspecify.annotations.Nullable;

public class StrongholdPieces$StraightStairsDown extends StrongholdPieces$StrongholdPiece {
   private static final int WIDTH = 5;
   private static final int HEIGHT = 11;
   private static final int DEPTH = 8;

   public StrongholdPieces$StraightStairsDown(final int genDepth, final RandomSource random, final BoundingBox boundingBox, final Direction direction) {
      super(StructurePieceType.STRONGHOLD_STRAIGHT_STAIRS_DOWN, genDepth, boundingBox);
      this.setOrientation(direction);
      this.entryDoor = this.randomSmallDoor(random);
   }

   public StrongholdPieces$StraightStairsDown(final CompoundTag tag) {
      super(StructurePieceType.STRONGHOLD_STRAIGHT_STAIRS_DOWN, tag);
   }

   @Override
   public void addChildren(final StructurePiece startPiece, final StructurePiecesBuilder builder, final RandomSource random) {
      this.generateSmallDoorChildForward((StrongholdPieces$StartPiece)startPiece, builder, random, 1, 1);
   }

   @Nullable
   public static StrongholdPieces$StraightStairsDown createPiece(
      final StructurePiecesBuilder builder,
      final RandomSource random,
      final int footX,
      final int footY,
      final int footZ,
      final Direction direction,
      final int genDepth
   ) {
      BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -7, 0, 5, 11, 8, direction);
      return isOkBox(box) && builder.findCollisionPiece(box) == null ? new StrongholdPieces$StraightStairsDown(genDepth, random, box, direction) : null;
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
      this.generateBox(level, chunkBB, 0, 0, 0, 4, 10, 7, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 7, 0);
      this.generateSmallDoor(level, random, chunkBB, StrongholdPieces$StrongholdPiece$SmallDoorType.OPENING, 1, 1, 7);
      BlockState stairs = Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.SOUTH);

      for (int i = 0; i < 6; i++) {
         this.placeBlock(level, stairs, 1, 6 - i, 1 + i, chunkBB);
         this.placeBlock(level, stairs, 2, 6 - i, 1 + i, chunkBB);
         this.placeBlock(level, stairs, 3, 6 - i, 1 + i, chunkBB);
         if (i < 5) {
            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 5 - i, 1 + i, chunkBB);
            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 5 - i, 1 + i, chunkBB);
            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 5 - i, 1 + i, chunkBB);
         }
      }
   }
}
