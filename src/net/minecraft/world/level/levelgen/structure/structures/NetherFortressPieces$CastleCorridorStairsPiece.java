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

public class NetherFortressPieces$CastleCorridorStairsPiece extends NetherFortressPieces$NetherBridgePiece {
   private static final int WIDTH = 5;
   private static final int HEIGHT = 14;
   private static final int DEPTH = 10;

   public NetherFortressPieces$CastleCorridorStairsPiece(final int genDepth, final BoundingBox boundingBox, final Direction direction) {
      super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_STAIRS, genDepth, boundingBox);
      this.setOrientation(direction);
   }

   public NetherFortressPieces$CastleCorridorStairsPiece(final CompoundTag tag) {
      super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_STAIRS, tag);
   }

   @Override
   public void addChildren(final StructurePiece startPiece, final StructurePiecesBuilder builder, final RandomSource random) {
      this.generateChildForward((NetherFortressPieces$StartPiece)startPiece, builder, random, 1, 0, true);
   }

   @Nullable
   public static NetherFortressPieces$CastleCorridorStairsPiece createPiece(
      final StructurePiecesBuilder builder, final int footX, final int footY, final int footZ, final Direction direction, final int genDepth
   ) {
      BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -7, 0, 5, 14, 10, direction);
      return isOkBox(box) && builder.findCollisionPiece(box) == null ? new NetherFortressPieces$CastleCorridorStairsPiece(genDepth, box, direction) : null;
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
      BlockState stairs = Blocks.NETHER_BRICK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.SOUTH);
      BlockState nsFence = Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, true).setValue(FenceBlock.SOUTH, true);

      for (int step = 0; step <= 9; step++) {
         int floor = Math.max(1, 7 - step);
         int roof = Math.min(Math.max(floor + 5, 14 - step), 13);
         int z = step;
         this.generateBox(level, chunkBB, 0, 0, step, 4, floor, step, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
         this.generateBox(level, chunkBB, 1, floor + 1, step, 3, roof - 1, step, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
         if (step <= 6) {
            this.placeBlock(level, stairs, 1, floor + 1, step, chunkBB);
            this.placeBlock(level, stairs, 2, floor + 1, step, chunkBB);
            this.placeBlock(level, stairs, 3, floor + 1, step, chunkBB);
         }

         this.generateBox(
            level, chunkBB, 0, roof, step, 4, roof, step, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false
         );
         this.generateBox(
            level, chunkBB, 0, floor + 1, step, 0, roof - 1, step, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false
         );
         this.generateBox(
            level, chunkBB, 4, floor + 1, step, 4, roof - 1, step, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false
         );
         if ((step & 1) == 0) {
            this.generateBox(level, chunkBB, 0, floor + 2, step, 0, floor + 3, step, nsFence, nsFence, false);
            this.generateBox(level, chunkBB, 4, floor + 2, step, 4, floor + 3, step, nsFence, nsFence, false);
         }

         for (int x = 0; x <= 4; x++) {
            this.fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB);
         }
      }
   }
}
