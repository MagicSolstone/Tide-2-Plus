package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jspecify.annotations.Nullable;

public class NetherFortressPieces$CastleStalkRoom extends NetherFortressPieces$NetherBridgePiece {
   private static final int WIDTH = 13;
   private static final int HEIGHT = 14;
   private static final int DEPTH = 13;

   public NetherFortressPieces$CastleStalkRoom(final int genDepth, final BoundingBox boundingBox, final Direction direction) {
      super(StructurePieceType.NETHER_FORTRESS_CASTLE_STALK_ROOM, genDepth, boundingBox);
      this.setOrientation(direction);
   }

   public NetherFortressPieces$CastleStalkRoom(final CompoundTag tag) {
      super(StructurePieceType.NETHER_FORTRESS_CASTLE_STALK_ROOM, tag);
   }

   @Override
   public void addChildren(final StructurePiece startPiece, final StructurePiecesBuilder builder, final RandomSource random) {
      this.generateChildForward((NetherFortressPieces$StartPiece)startPiece, builder, random, 5, 3, true);
      this.generateChildForward((NetherFortressPieces$StartPiece)startPiece, builder, random, 5, 11, true);
   }

   @Nullable
   public static NetherFortressPieces$CastleStalkRoom createPiece(
      final StructurePiecesBuilder builder, final int footX, final int footY, final int footZ, final Direction direction, final int genDepth
   ) {
      BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -5, -3, 0, 13, 14, 13, direction);
      return isOkBox(box) && builder.findCollisionPiece(box) == null ? new NetherFortressPieces$CastleStalkRoom(genDepth, box, direction) : null;
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
      this.generateBox(level, chunkBB, 0, 3, 0, 12, 4, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 0, 5, 0, 12, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 0, 5, 0, 1, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 11, 5, 0, 12, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 2, 5, 11, 4, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 8, 5, 11, 10, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 5, 9, 11, 7, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 2, 5, 0, 4, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 8, 5, 0, 10, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 5, 9, 0, 7, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 2, 11, 2, 10, 12, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      BlockState weFence = Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.WEST, true).setValue(FenceBlock.EAST, true);
      BlockState nsFence = Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, true).setValue(FenceBlock.SOUTH, true);
      BlockState nswFence = nsFence.setValue(FenceBlock.WEST, true);
      BlockState nseFence = nsFence.setValue(FenceBlock.EAST, true);

      for (int i = 1; i <= 11; i += 2) {
         this.generateBox(level, chunkBB, i, 10, 0, i, 11, 0, weFence, weFence, false);
         this.generateBox(level, chunkBB, i, 10, 12, i, 11, 12, weFence, weFence, false);
         this.generateBox(level, chunkBB, 0, 10, i, 0, 11, i, nsFence, nsFence, false);
         this.generateBox(level, chunkBB, 12, 10, i, 12, 11, i, nsFence, nsFence, false);
         this.placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), i, 13, 0, chunkBB);
         this.placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), i, 13, 12, chunkBB);
         this.placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), 0, 13, i, chunkBB);
         this.placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), 12, 13, i, chunkBB);
         if (i != 11) {
            this.placeBlock(level, weFence, i + 1, 13, 0, chunkBB);
            this.placeBlock(level, weFence, i + 1, 13, 12, chunkBB);
            this.placeBlock(level, nsFence, 0, 13, i + 1, chunkBB);
            this.placeBlock(level, nsFence, 12, 13, i + 1, chunkBB);
         }
      }

      this.placeBlock(level, Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, true).setValue(FenceBlock.EAST, true), 0, 13, 0, chunkBB);
      this.placeBlock(level, Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.SOUTH, true).setValue(FenceBlock.EAST, true), 0, 13, 12, chunkBB);
      this.placeBlock(
         level, Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.SOUTH, true).setValue(FenceBlock.WEST, true), 12, 13, 12, chunkBB
      );
      this.placeBlock(level, Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, true).setValue(FenceBlock.WEST, true), 12, 13, 0, chunkBB);

      for (int z = 3; z <= 9; z += 2) {
         this.generateBox(level, chunkBB, 1, 7, z, 1, 8, z, nswFence, nswFence, false);
         this.generateBox(level, chunkBB, 11, 7, z, 11, 8, z, nseFence, nseFence, false);
      }

      BlockState stairs = Blocks.NETHER_BRICK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.NORTH);

      for (int ix = 0; ix <= 6; ix++) {
         int z = ix + 4;

         for (int x = 5; x <= 7; x++) {
            this.placeBlock(level, stairs, x, 5 + ix, z, chunkBB);
         }

         if (z >= 5 && z <= 8) {
            this.generateBox(level, chunkBB, 5, 5, z, 7, ix + 4, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
         } else if (z >= 9 && z <= 10) {
            this.generateBox(level, chunkBB, 5, 8, z, 7, ix + 4, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
         }

         if (ix >= 1) {
            this.generateBox(level, chunkBB, 5, 6 + ix, z, 7, 9 + ix, z, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
         }
      }

      for (int x = 5; x <= 7; x++) {
         this.placeBlock(level, stairs, x, 12, 11, chunkBB);
      }

      this.generateBox(level, chunkBB, 5, 6, 7, 5, 7, 7, nseFence, nseFence, false);
      this.generateBox(level, chunkBB, 7, 6, 7, 7, 7, 7, nswFence, nswFence, false);
      this.generateBox(level, chunkBB, 5, 13, 12, 7, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 2, 5, 2, 3, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 2, 5, 9, 3, 5, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 2, 5, 4, 2, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 9, 5, 2, 10, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 9, 5, 9, 10, 5, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 10, 5, 4, 10, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      BlockState eastStairs = stairs.setValue(StairBlock.FACING, Direction.EAST);
      BlockState westStairs = stairs.setValue(StairBlock.FACING, Direction.WEST);
      this.placeBlock(level, westStairs, 4, 5, 2, chunkBB);
      this.placeBlock(level, westStairs, 4, 5, 3, chunkBB);
      this.placeBlock(level, westStairs, 4, 5, 9, chunkBB);
      this.placeBlock(level, westStairs, 4, 5, 10, chunkBB);
      this.placeBlock(level, eastStairs, 8, 5, 2, chunkBB);
      this.placeBlock(level, eastStairs, 8, 5, 3, chunkBB);
      this.placeBlock(level, eastStairs, 8, 5, 9, chunkBB);
      this.placeBlock(level, eastStairs, 8, 5, 10, chunkBB);
      this.generateBox(level, chunkBB, 3, 4, 4, 4, 4, 8, Blocks.SOUL_SAND.defaultBlockState(), Blocks.SOUL_SAND.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 8, 4, 4, 9, 4, 8, Blocks.SOUL_SAND.defaultBlockState(), Blocks.SOUL_SAND.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 3, 5, 4, 4, 5, 8, Blocks.NETHER_WART.defaultBlockState(), Blocks.NETHER_WART.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 8, 5, 4, 9, 5, 8, Blocks.NETHER_WART.defaultBlockState(), Blocks.NETHER_WART.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 4, 2, 0, 8, 2, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 0, 2, 4, 12, 2, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 4, 0, 0, 8, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 4, 0, 9, 8, 1, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 0, 0, 4, 3, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 9, 0, 4, 12, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);

      for (int x = 4; x <= 8; x++) {
         for (int z = 0; z <= 2; z++) {
            this.fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB);
            this.fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, 12 - z, chunkBB);
         }
      }

      for (int x = 0; x <= 2; x++) {
         for (int z = 4; z <= 8; z++) {
            this.fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB);
            this.fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), 12 - x, -1, z, chunkBB);
         }
      }
   }
}
