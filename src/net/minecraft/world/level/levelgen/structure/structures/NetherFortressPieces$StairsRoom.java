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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jspecify.annotations.Nullable;

public class NetherFortressPieces$StairsRoom extends NetherFortressPieces$NetherBridgePiece {
   private static final int WIDTH = 7;
   private static final int HEIGHT = 11;
   private static final int DEPTH = 7;

   public NetherFortressPieces$StairsRoom(final int genDepth, final BoundingBox boundingBox, final Direction direction) {
      super(StructurePieceType.NETHER_FORTRESS_STAIRS_ROOM, genDepth, boundingBox);
      this.setOrientation(direction);
   }

   public NetherFortressPieces$StairsRoom(final CompoundTag tag) {
      super(StructurePieceType.NETHER_FORTRESS_STAIRS_ROOM, tag);
   }

   @Override
   public void addChildren(final StructurePiece startPiece, final StructurePiecesBuilder builder, final RandomSource random) {
      this.generateChildRight((NetherFortressPieces$StartPiece)startPiece, builder, random, 6, 2, false);
   }

   @Nullable
   public static NetherFortressPieces$StairsRoom createPiece(
      final StructurePiecesBuilder builder, final int footX, final int footY, final int footZ, final int genDepth, final Direction direction
   ) {
      BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -2, 0, 0, 7, 11, 7, direction);
      return isOkBox(box) && builder.findCollisionPiece(box) == null ? new NetherFortressPieces$StairsRoom(genDepth, box, direction) : null;
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
      this.generateBox(level, chunkBB, 0, 0, 0, 6, 1, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 0, 2, 0, 6, 10, 6, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 0, 2, 0, 1, 8, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 5, 2, 0, 6, 8, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 0, 2, 1, 0, 8, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 6, 2, 1, 6, 8, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 1, 2, 6, 5, 8, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      BlockState weFence = Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.WEST, true).setValue(FenceBlock.EAST, true);
      BlockState nsFence = Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, true).setValue(FenceBlock.SOUTH, true);
      this.generateBox(level, chunkBB, 0, 3, 2, 0, 5, 4, nsFence, nsFence, false);
      this.generateBox(level, chunkBB, 6, 3, 2, 6, 5, 2, nsFence, nsFence, false);
      this.generateBox(level, chunkBB, 6, 3, 4, 6, 5, 4, nsFence, nsFence, false);
      this.placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), 5, 2, 5, chunkBB);
      this.generateBox(level, chunkBB, 4, 2, 5, 4, 3, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 3, 2, 5, 3, 4, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 2, 2, 5, 2, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 1, 2, 5, 1, 6, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 1, 7, 1, 5, 7, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 6, 8, 2, 6, 8, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 2, 6, 0, 4, 8, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 2, 5, 0, 4, 5, 0, weFence, weFence, false);

      for (int x = 0; x <= 6; x++) {
         for (int z = 0; z <= 6; z++) {
            this.fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB);
         }
      }
   }
}
